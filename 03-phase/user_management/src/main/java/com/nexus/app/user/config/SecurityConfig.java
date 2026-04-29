package com.nexus.app.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the User Management application.
 * 
 * This class configures Spring Security settings including:
 * - CSRF protection (disabled for API endpoints)
 * - Authorization rules for API endpoints and Swagger documentation
 * - Public access to Swagger UI and API documentation
 * - Public access to API endpoints
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * 
     * @param httpSecurity the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            // Disable CSRF protection for API requests
            .csrf(csrf -> csrf.disable())
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow public access to Swagger/API documentation
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // Allow public access to API endpoints
                .requestMatchers("/api/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            );

        return httpSecurity.build();
    }
}
