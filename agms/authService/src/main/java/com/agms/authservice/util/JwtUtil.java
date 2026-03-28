package com.agms.authservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${secretKey1}")
    private String secretKey1;
    @Value("${secretKey2}")
    private String secretKey2;
    @Value("${accessToken.expiration}")
    private long accessTokenExpiration;
    @Value("${refreshToken.expiration}")
    private long refreshTokenExpiration;

    private static final String ROLE_CLAIM = "role";

    public String generateAccessToken(String email, Role role) {
        return buildToken(email, role, accessTokenExpiration, getAccessSecretKey());
    }

    public String generateRefreshToken(String email, Role role) {
        return buildToken(email, role, refreshTokenExpiration, getRefreshSecretKey());
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessSecretKey());
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSecretKey());
    }

    public String extractEmailFromAccessToken(String token) {
        return extractClaims(token, getAccessSecretKey()).getSubject();
    }

    public String extractEmailFromRefreshToken(String token) {
        return extractClaims(token, getRefreshSecretKey()).getSubject();
    }

    public Role extractRoleFromAccessToken(String token) {
        return extractRole(token, getAccessSecretKey());
    }

    public Role extractRoleFromRefreshToken(String token) {
        return extractRole(token, getRefreshSecretKey());
    }

    private String buildToken(String email, Role role, long expiration, SecretKey secretKey) {
        return Jwts.builder()
                .setSubject(email)
                .claim(ROLE_CLAIM, role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean validateToken(String token, SecretKey secretKey) {
        try {
            extractClaims(token, secretKey);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractClaims(String token, SecretKey secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Role extractRole(String token, SecretKey secretKey) {
        String roleValue = extractClaims(token, secretKey).get(ROLE_CLAIM, String.class);
        return Role.valueOf(roleValue);
    }

    private SecretKey getAccessSecretKey() {
        return Keys.hmacShaKeyFor(secretKey1.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getRefreshSecretKey() {
        return Keys.hmacShaKeyFor(secretKey2.getBytes(StandardCharsets.UTF_8));
    }

}
