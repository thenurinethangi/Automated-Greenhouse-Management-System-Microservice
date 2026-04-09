package com.agms.zoneservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.agms.zoneservice.service.IoTAuthService;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

    private final IoTAuthService authService;

    public FeignConfig(@Lazy IoTAuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // 1. Forward headers from the incoming request (API Gateway)
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                String email = request.getHeader("User-Email");
                String role = request.getHeader("User-Role");
                
                if (email != null) {
                    requestTemplate.header("User-Email", email);
                }
                if (role != null) {
                    requestTemplate.header("User-Role", role);
                }
            }

            // 2. Add Bearer Authorization token for IoT service
            String token = authService.getAccessToken();
            if (token != null && !requestTemplate.url().contains("/auth/")) {
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }
}
