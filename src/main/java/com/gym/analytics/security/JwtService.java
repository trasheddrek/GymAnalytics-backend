package com.gym.analytics.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(this.key)
                .compact();
    }

    public String extraerId(String token) {
        return Jwts.parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean esTokenValido(String token, UserDetails userDetails) {
        try {
            String id = extraerId(token);

            Claims claims = Jwts.parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            boolean noExpirado = claims.getExpiration().after(new Date());

            return id.equals(userDetails.getUsername()) && noExpirado;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}