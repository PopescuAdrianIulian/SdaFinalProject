package com.example.orderservice.service;

import com.example.orderservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${EXPIRATION_TIME}")
    private long EXPIRATION_TIME;

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public JwtService() {
    }

    public String generateToken(User user) {
        System.out.println("Generating token for user: " + user.getEmail() + ", type: " + user.getUserType());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", user.getUserType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token signature", e);
        }

        return claims.getSubject();
    }

}

