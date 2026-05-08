package com.smart.billing.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Get the Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Validate that the header exists and begins with "Bearer"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token (position 7 onwards) and the user's email
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractEmail(jwt);

        // Check if the user is not already authenticated in the security context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Check if the token is valid (not expired and with a correct signature)
            if (jwtService.isTokenValid(jwt, userEmail)) {
                
                // Create the authentication object for Spring Security
                // We're using an empty list of authorities for now (you can load roles here)
                UserDetails userDetails = new User(userEmail, "", Collections.emptyList());
                
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Enrich authentication with request details (IP, Session)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the user as "Authenticated" in the system
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue to the next filter in the chain
        filterChain.doFilter(request, response);
    }

}
