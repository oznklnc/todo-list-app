package com.demo.todo.list.app.security;

import com.demo.todo.list.app.constant.TodoListConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                        .filter(authHeader -> authHeader.startsWith(TodoListConstants.TOKEN_PREFIX))
                                .map(authHeader -> authHeader.substring(TodoListConstants.TOKEN_PREFIX.length()))
                                        .filter()
        filterChain.doFilter(request, response);

    }
}
