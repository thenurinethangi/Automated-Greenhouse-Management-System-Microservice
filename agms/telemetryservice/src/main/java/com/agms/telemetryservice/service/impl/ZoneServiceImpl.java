package com.agms.telemetryservice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.agms.telemetryservice.service.IoTAuthService;
import com.agms.telemetryservice.service.ZoneService;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final WebClient webClient;
    private final IoTAuthService authService;

    public ZoneServiceImpl(WebClient.Builder builder, IoTAuthService authService,
            @Value("${zone.service.base-url}") String iotBaseUrl) {
        this.webClient = builder.baseUrl(iotBaseUrl).build();
        this.authService = authService;
    }

    @Override
    public List<Map<String, Object>> getAllZones() {

        String accessToken = authService.getAccessToken();

        List<Map<String, Object>> response = webClient.get()
                .uri("/api/zones")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                })
                .block();

        return response;
    }

}
