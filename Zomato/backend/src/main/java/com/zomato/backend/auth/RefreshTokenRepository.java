package com.zomato.backend.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    
    Optional<RefreshToken> findByTokenHashAndIsRevokedFalse(String tokenHash);

    @Modifying
    @Query("""
            UPDATE RefreshToken rt
            SET rt.isRevoked = true, rt.revokedAt = :now
            WHERE rt.user.id = :userId AND rt.isRevoked = false
            """)
    void revokeAllUserTokens(Long userId,LocalDateTime now);

    @Modifying
    @Query("""
            DELETE FROM RefreshToken rt
            WHERE rt.expiresAt < :cutoff OR rt.isRevoked = true
            """)
    void deleteExpiredTokens(LocalDateTime cutoff);
}
