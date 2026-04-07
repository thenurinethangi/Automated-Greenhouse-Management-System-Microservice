package com.agms.telemetryservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.agms.telemetryservice.service.AutomationService;
import com.agms.telemetryservice.service.IoTAuthService;

@Service
public class AutomationServiceImpl implements AutomationService {

    private final WebClient webClient;
    private final IoTAuthService authService;

    public AutomationServiceImpl(WebClient.Builder builder, IoTAuthService authService,
            @Value("${automation.service.base-url}") String automationServiceBaseUrl) {
        this.webClient = builder.baseUrl(automationServiceBaseUrl).build();
        this.authService = authService;
    }

    @Override
    public Map<String, Object> callAutomationServiceToApplyLogic(Map<String, Object> data) {

        Map<String, Object> response = webClient.post()
                .uri("/api/automation/process")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        return response;
    }

}
