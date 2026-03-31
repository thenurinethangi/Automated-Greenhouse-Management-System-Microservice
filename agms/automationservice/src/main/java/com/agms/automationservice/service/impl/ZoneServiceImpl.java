package com.agms.automationservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.agms.automationservice.service.ZoneService;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final WebClient webClient;

    public ZoneServiceImpl(WebClient.Builder builder,
            @Value("${zone.service.base-url}") String zoneServiceBaseUrl) {
        this.webClient = builder.baseUrl(zoneServiceBaseUrl).build();
    }

    public Map<String, Object> getZoneData(String zoneId) {

        Map<String, Object> response = webClient.get()
                .uri("/api/zones/" + zoneId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        return response;
    }
}
