package com.agms.apiGetway.filters;

import com.agms.apiGetway.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid JWT token");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateAccessToken(token)) {
            throw new ServletException("Invalid JWT token");
        }

        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        request.setAttribute("email", email);
        request.setAttribute("role", role);

        filterChain.doFilter(request, response);
    }
}
