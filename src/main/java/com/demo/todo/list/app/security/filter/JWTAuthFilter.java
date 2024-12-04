package com.demo.todo.list.app.security.filter;

import com.demo.todo.list.app.constant.TodoListConstants;
import com.demo.todo.list.app.security.jwt.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    @Value("${jwt.token.filter.urls}")
    private List<String> notFilteredUrls;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(TodoListConstants.BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(TodoListConstants.BEARER_PREFIX.length()))
                .filter(jwtHelper::validateToken)
                .map(jwtHelper::getUsername)
                .map(userDetailsService::loadUserByUsername)
                .ifPresent(this::buildUsernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);

    }

    private void buildUsernamePasswordAuthenticationToken(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getServletPath();
        return notFilteredUrls.stream().anyMatch(pattern -> matches(pattern, url));
    }

    private boolean matches(String pattern, String url) {
        if (pattern.endsWith("/**")) {
            return url.startsWith(pattern.substring(0, pattern.length() - 3));
        }
        return url.equals(pattern);
    }

}
