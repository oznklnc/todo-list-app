package com.demo.todo.list.app.security.jwt;

import org.springframework.security.core.Authentication;

public interface JwtHelper {

    boolean validateToken(String token);
    String getUsername(String token);
    String generateJwtToken(Authentication authentication);
}
