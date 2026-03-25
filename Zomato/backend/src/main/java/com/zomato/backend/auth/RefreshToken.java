package com.zomato.backend.auth;

import com.zomato.backend.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "token_hash",nullable = false,unique = true,length = 64)
    private String tokenHash;

    @Column(name = "device_info",length = 255)
    private String deviceInfo;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "expires_at",nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_revoked",nullable = false)
    @Builder.Default
    private Boolean isRevoked =false;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "created_at",nullable = false,updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
