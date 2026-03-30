package com.agms.telemetryservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.agms.telemetryservice.service.IoTAuthService;

@Service
public class IoTAuthServiceImpl implements IoTAuthService {

    private String accessToken;
    private String refreshToken;

    @Value("${iot.username}")
    private String username;

    @Value("${iot.password}")
    private String password;

    private final WebClient webClient;

    public IoTAuthServiceImpl(WebClient.Builder builder, @Value("${iot.base-url}") String iotBaseUrl) {
        this.webClient = builder.baseUrl(iotBaseUrl).build();
    }

    @Override
    public String getAccessToken() {
        if (accessToken == null) {
            login();
        }
        return accessToken;
    }

    @Override
    public String refreshAccessToken() {
        if (refreshToken == null) {
            login();
            return accessToken;
        }

        Map<String, String> request = new HashMap<>();
        request.put("refreshToken", refreshToken);

        Map<String, Object> response = webClient.post()
                .uri("/auth/refresh")
                .bodyValue(request)
                .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                .block();

        this.accessToken = (String) response.get("accessToken");

        Object newRefreshToken = response.get("refreshToken");
        if (newRefreshToken instanceof String) {
            this.refreshToken = (String) newRefreshToken;
        }

        return this.accessToken;
    }

    private void login() {
        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        Map<String, Object> response = webClient.post()
                .uri("/auth/login")
                .bodyValue(request)
                .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
            })
                .block();

        this.accessToken = (String) response.get("accessToken");
        this.refreshToken = (String) response.get("refreshToken");
    }
}
