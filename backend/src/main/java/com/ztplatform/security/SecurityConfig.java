package com.ztplatform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://192.168.0.143:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/verify/**",
                    "/api/credentials/*/qr",
                    "/api/credentials/*/pdf",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // Student endpoints (specific paths - must come before broader patterns)
                .requestMatchers("/api/documents/my")
                .hasAuthority("ROLE_STUDENT")
                .requestMatchers("/api/students/me")
                .hasAuthority("ROLE_STUDENT")
                .requestMatchers("/api/requests/my")
                .hasAuthority("ROLE_STUDENT")
                .requestMatchers("/api/credentials/my")
                .hasAuthority("ROLE_STUDENT")
                .requestMatchers(HttpMethod.POST, "/api/requests")
                .hasAuthority("ROLE_STUDENT")

                // Admin endpoints
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")

                // Admin/Verifier endpoints
                .requestMatchers(HttpMethod.GET, "/api/requests")
                .hasAnyRole("ADMIN", "VERIFIER")
                .requestMatchers("/api/requests/{id}/**", "/api/requests/student/{studentId}")
                .hasAnyRole("ADMIN", "VERIFIER")

                // Admin credential management
                .requestMatchers(HttpMethod.GET, "/api/credentials")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/credentials/{id}/revoke")
                .hasRole("ADMIN")

                .requestMatchers("/api/students", "/api/students/**")
                .hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest()
                .authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}