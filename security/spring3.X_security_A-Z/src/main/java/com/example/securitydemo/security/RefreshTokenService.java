package com.example.securitydemo.security;

import com.example.securitydemo.dto.TokenResponse;
import com.example.securitydemo.entity.AppUser;
import com.example.securitydemo.entity.RefreshToken;
import com.example.securitydemo.repository.RefreshTokenRepository;
import com.example.securitydemo.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class RefreshTokenService {

    private static final long REFRESH_DAYS = 7;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    private String generateRandomToken() {

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashToken(String token) {

        try {
            byte[] hash = MessageDigest
                    .getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String createRefreshToken(AppUser user) {

        String rawToken = generateRandomToken();

        RefreshToken refreshToken = new RefreshToken(
                hashToken(rawToken),
                user,
                Instant.now().plus(REFRESH_DAYS, ChronoUnit.DAYS)
        );

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    @Transactional
    public TokenResponse createTokens(String username) {

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED
                        )
                );

        String refreshToken = createRefreshToken(user);

        UserDetails details =
                userDetailsService.loadUserByUsername(username);

        var authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        details,
                        null,
                        details.getAuthorities()
                );

        String accessToken =
                jwtService.generateToken(authentication);

        return new TokenResponse(
                accessToken,
                refreshToken,
                "Bearer",
                900
        );
    }

    @Transactional
    public TokenResponse refresh(String rawToken) {

        RefreshToken storedToken = refreshTokenRepository
                //.findByTokenHash(hashToken(rawToken))
                .findByTokenHashForUpdate(hashToken(rawToken))
		.orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Refresh token invalide"
                        )
                );

        if (storedToken.isRevoked()
                || !storedToken.getExpiresAt().isAfter(Instant.now())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Refresh token expiré ou révoqué"
            );
        }

        storedToken.revoke();

        return createTokens(storedToken.getUser().getUsername());
    }

    @Transactional
    public void logout(String rawToken) {

        refreshTokenRepository
                .findByTokenHash(hashToken(rawToken))
                .ifPresent(RefreshToken::revoke);
    }
}
