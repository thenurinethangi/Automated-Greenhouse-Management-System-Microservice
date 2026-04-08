package com.agms.apiGetway.config;

// import com.agms.apiGetway.exception.CustomAuthenticationEntryPoint;
import com.agms.apiGetway.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.http.HttpStatus;

// import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

        // private final JwtFilter jwtAuthFilter;
        // private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

                return http
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .authorizeExchange(exchange -> exchange
                                                .pathMatchers("/api/auth/**").permitAll()
                                                .anyExchange().authenticated())
                                .exceptionHandling(ex -> ex
                                                .authenticationEntryPoint((exchange, ex2) -> {
                                                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                                        return exchange.getResponse().setComplete();
                                                }))
                                .build();
        }

        // @Bean
        // public CorsConfigurationSource corsConfigurationSource() {
        // CorsConfiguration config = new CorsConfiguration();
        // config.setAllowedOrigins(List.of("*"));
        // config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // config.setAllowedHeaders(List.of("*"));
        // config.setAllowCredentials(true);

        // UrlBasedCorsConfigurationSource source = new
        // UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", config);
        // return source;
        // }
}
