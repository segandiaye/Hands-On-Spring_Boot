package com.example.securitydemo.controller;

import com.example.securitydemo.dto.LoginRequest;
import com.example.securitydemo.dto.RegisterRequest;
import com.example.securitydemo.service.AuthService;
import com.example.securitydemo.security.JwtService;
import com.example.securitydemo.dto.RefreshRequest;
import com.example.securitydemo.dto.TokenResponse;
import com.example.securitydemo.security.RefreshTokenService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
	    AuthenticationManager authenticationManager,
	    AuthService authService,
	    JwtService jwtService,
	    RefreshTokenService refreshTokenService
	    ) {
	this.authenticationManager = authenticationManager;
	this.authService = authService;
	this.jwtService = jwtService;
	this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
	public ResponseEntity<?> register(
		@Valid @RequestBody RegisterRequest request
		) {

	    authService.register(request);

	    return ResponseEntity
		.status(HttpStatus.CREATED)
		.body(Map.of(
			    "message", "Compte créé avec succès"
			    ));
	}

    //@PostMapping("/login")
	//public ResponseEntity<?> login(
	//	@RequestBody LoginRequest request
	//	) {
	//    try {
	//	Authentication authentication =
		   // authenticationManager.authenticate(
		//	    UsernamePasswordAuthenticationToken
		//	    .unauthenticated(
		//		request.username(),
		//		request.password()
		//		)
		//	    );
		//String token = jwtService.generateToken(authentication);
		//return ResponseEntity.ok(
		//	Map.of(
		//	    "access_token", token,
		//	    "token_type", "Bearer",
		//	    "expires_in", 900,
		//	    "authorities", authentication.getAuthorities()
		//	    .stream()
		//	    .map(authority -> authority.getAuthority())
		//	    .toList()
		//	    )
		//	);
	    //} catch (AuthenticationException ex) {
		//return ResponseEntity
		  //  .status(HttpStatus.UNAUTHORIZED)
		    //.body(Map.of(
		//		"message", "Identifiants incorrects"
		//		));
	    //}
	//}

    @PostMapping("/login")
	public ResponseEntity<?> login(
		@RequestBody LoginRequest request
		) {
	    try {
		Authentication authentication =
		    authenticationManager.authenticate(
			    UsernamePasswordAuthenticationToken
			    .unauthenticated(
				request.username(),
				request.password()
				)
			    );

		TokenResponse tokens =
		    refreshTokenService.createTokens(
			    authentication.getName()
			    );

		return ResponseEntity.ok(tokens);

	    } catch (AuthenticationException ex) {
		return ResponseEntity
		    .status(HttpStatus.UNAUTHORIZED)
		    .body(Map.of(
				"message", "Identifiants incorrects"
				));
	    }
	}

    @PostMapping("/refresh")
	public TokenResponse refresh(
		@Valid @RequestBody RefreshRequest request
		) {
	    return refreshTokenService.refresh(
		    request.refreshToken()
		    );
	}

    @PostMapping("/logout")
	public ResponseEntity<Void> logout(
		@Valid @RequestBody RefreshRequest request
		) {
	    refreshTokenService.logout(
		    request.refreshToken()
		    );

	    return ResponseEntity.noContent().build();
	}

}
