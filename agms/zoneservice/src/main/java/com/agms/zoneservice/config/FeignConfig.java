package com.agms.zoneservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {

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
        };
    }
}
