package com.zomato.backend.auth.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.zomato.backend.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiry}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiry;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",user.getRole().name());
        claims.put("email",user.getEmail());
        claims.put("name", user.getFullName());

        return Jwts.builder()
                    .claims(claims)
                    .subject(user.getId().toString())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                    .signWith(getSigningKey())
                    .compact();
    }

    public String generateRefreshToken(User user){
        return Jwts.builder()
                   .subject(user.getId().toString())
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                   .signWith(getSigningKey())
                   .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public String extractUserId(String token){
        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).get("email", String.class);
    }

    public boolean isTokenValid(String token){
        try {
            extractAllClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.debug("JWT expired: {}",e.getMessage());
            return false;
        }catch (JwtException e){
            log.warn("Invalid JWT: {}",e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
