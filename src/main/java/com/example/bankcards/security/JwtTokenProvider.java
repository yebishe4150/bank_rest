package com.example.bankcards.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bankcards.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties properties;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getExpiration());

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256(properties.getSecret()));
    }

    public String getUsername(String token) {
        return JWT.require(Algorithm.HMAC256(properties.getSecret()))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            JWT.require(Algorithm.HMAC256(properties.getSecret()))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
