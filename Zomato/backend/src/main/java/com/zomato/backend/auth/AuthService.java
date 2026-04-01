package com.zomato.backend.auth;

import com.zomato.backend.auth.dto.*;
import com.zomato.backend.auth.util.JwtUtil;
import com.zomato.backend.common.enums.UserRole;
import com.zomato.backend.common.exception.BusinessException;
import com.zomato.backend.common.exception.ResourceNotFoundException;
import com.zomato.backend.user.User;
import com.zomato.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiryMs;

    // ─── REGISTER ─────────────────────────────────────────────
    @Transactional
    // @Transactional: if ANYTHING in this method throws an exception,
    // ALL database changes are rolled back. User creation + token storage
    // are atomic — either both succeed or neither does.
    public AuthResponse register(RegisterRequest request) {

        // 1. Normalize email to lowercase
        String email = request.getEmail().toLowerCase().trim();

        // 2. Check email isn't already taken
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(
                "An account with this email already exists",
                HttpStatus.CONFLICT
            );
        }

        // 3. Check phone isn't already taken (if provided)
        if (request.getPhone() != null && !request.getPhone().isBlank()
                && userRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(
                "An account with this phone number already exists",
                HttpStatus.CONFLICT
            );
        }

        // 4. Build and save the user
        User user = User.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                // BCrypt hash is computed here — never stored plain text
                .phone(request.getPhone())
                .role(UserRole.CUSTOMER)
                .isEmailVerified(false) // must verify email before login works
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("New user registered: {} (id={})", email, user.getId());

        // 5. Generate tokens
        String accessToken  = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // 6. Persist refresh token hash (we hash it so raw token isn't in DB)
        saveRefreshToken(user, refreshToken, null, null);

        // 7. TODO: Send email verification email (Phase 5 — Email service)

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    // ─── LOGIN ────────────────────────────────────────────────
    @Transactional
    public AuthResponse login(LoginRequest request, String deviceInfo, String ipAddress) {

        String email = request.getEmail().toLowerCase().trim();

        // Spring Security's AuthenticationManager handles:
        // - Loading user via UserDetailsService.loadUserByUsername
        // - Comparing hashed passwords via BCrypt
        // - Checking isEnabled(), isAccountNonLocked(), etc.
        // If anything fails, it throws an exception
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Deliberately vague — don't tell attackers if email exists
            throw new BusinessException("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", 0L));

        // Update last login timestamp
        userRepository.updateLastLoginAt(user.getId(),LocalDateTime.now());

        // Generate fresh tokens
        String accessToken  = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        // Save new refresh token
        saveRefreshToken(user, refreshToken, deviceInfo, ipAddress);

        log.info("User logged in: {} from {}", email, ipAddress);

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    // ─── REFRESH TOKEN ────────────────────────────────────────
    @Transactional
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {

        String incomingToken = request.getRefreshToken();

        // Validate the JWT signature and expiry
        if (!jwtUtil.isTokenValid(incomingToken)) {
            throw new BusinessException("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }

        // Look up the token hash in DB
        String tokenHash = hashToken(incomingToken);
        RefreshToken stored = refreshTokenRepository
                .findByTokenHashAndIsRevokedFalse(tokenHash)
                .orElseThrow(() -> new BusinessException(
                    "Refresh token not recognized. Please log in again.",
                    HttpStatus.UNAUTHORIZED
                ));

        // Check DB-level expiry (double check)
        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            stored.setRevoked(true);
            refreshTokenRepository.save(stored);
            throw new BusinessException("Refresh token expired. Please log in again.", HttpStatus.UNAUTHORIZED);
        }

        User user = stored.getUser();

        // ROTATION: revoke old token, issue new one
        // This means each refresh token can only be used once.
        // If a token is stolen and used, the next use by the legitimate
        // user will fail (token already revoked) — and you can detect the attack.
        stored.setRevoked(true);
        stored.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(stored);

        String newAccessToken  = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user);
        saveRefreshToken(user, newRefreshToken, stored.getDeviceInfo(), stored.getIpAddress());

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    // ─── LOGOUT ───────────────────────────────────────────────
    @Transactional
    public void logout(String refreshToken) {
        String tokenHash = hashToken(refreshToken);
        refreshTokenRepository.findByTokenHashAndIsRevokedFalse(tokenHash)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    token.setRevokedAt(LocalDateTime.now());
                    refreshTokenRepository.save(token);
                });
    }

    // ─── LOGOUT ALL DEVICES ───────────────────────────────────
    @Transactional
    public void logoutAllDevices(Long userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("All sessions revoked for user id={}", userId);
    }

    // ─── Private Helpers ──────────────────────────────────────

    private void saveRefreshToken(User user, String rawToken,
                                   String deviceInfo, String ipAddress) {
        RefreshToken token = RefreshToken.builder()
                .user(user)
                .tokenHash(hashToken(rawToken))
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiryMs / 1000))
                .isRevoked(false)
                .build();
        refreshTokenRepository.save(token);
    }

    private String  hashToken(String token) {
        // SHA-256 hash of the token string
        // Same token always produces same hash — deterministic lookup
        // But hash cannot be reversed to get the original token
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isEmailVerified(user.isEmailVerified())
                .build();
    }
}