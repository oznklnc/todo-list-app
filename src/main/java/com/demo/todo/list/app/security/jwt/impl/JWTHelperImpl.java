package com.demo.todo.list.app.security.jwt.impl;

import com.demo.todo.list.app.config.properties.SecurityProperty;
import com.demo.todo.list.app.model.security.TodoListUserDetails;
import com.demo.todo.list.app.security.jwt.JwtHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JWTHelperImpl implements JwtHelper {

    private final SecurityProperty securityProperty;
    private final SecretKey secretKey;

    public JWTHelperImpl(SecurityProperty securityProperty) {
        this.securityProperty = securityProperty;
        this.secretKey = Keys.hmacShaKeyFor(securityProperty.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaims(token);

            Instant now = Instant.now();
            Date exp = claims.getExpiration();
            return exp.after(Date.from(now));
        } catch (JwtException e) {
            log.error("Error occurred while validating token: ", e);
            return false;
        }

    }

    @Override
    public String getUsername(String token) {
        try {
            Claims claims = getClaims(token);
            return String.valueOf(claims.get("username"));
        } catch (JwtException e) {
            log.error("Error occurred while getting subject from token: ", e);
            return null;
        }
    }

    @Override
    public String generateJwtToken(Authentication authentication) {
        TodoListUserDetails userDetails = (TodoListUserDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(userDetails.getUsername())
                .claim("username", userDetails.getUsername())
                .issuedAt(new Date(now.toEpochMilli()))
                .expiration(new Date(now.plus(securityProperty.getDuration()).toEpochMilli()))
                .signWith(secretKey)
                .compact();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
