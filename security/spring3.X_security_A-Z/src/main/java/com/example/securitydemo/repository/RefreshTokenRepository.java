package com.example.securitydemo.repository;

import com.example.securitydemo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository
extends JpaRepository<RefreshToken, Long> {

    // Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("""
		SELECT rt
		FROM RefreshToken rt
		WHERE rt.tokenHash = :tokenHash
		""")
	Optional<RefreshToken> findByTokenHashForUpdate(
		@Param("tokenHash") String tokenHash
		);
}
