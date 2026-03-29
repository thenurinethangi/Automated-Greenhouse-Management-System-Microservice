package com.agms.zoneservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.agms.zoneservice.service.IoTAuthService;
import com.agms.zoneservice.service.IoTDeviceService;

@Service
public class IoTDeviceServiceImpl implements IoTDeviceService {

    private final WebClient webClient;
    private final IoTAuthService authService;

    public IoTDeviceServiceImpl(WebClient.Builder builder, IoTAuthService authService,
            @Value("${iot.base-url}") String iotBaseUrl) {
        this.webClient = builder.baseUrl(iotBaseUrl).build();
        this.authService = authService;
    }

    @Override
    public Map<String, String> registerDevice(String name, String zoneId) {
        Map<String, String> request = new HashMap<>();
        request.put("name", name);
        request.put("zoneId", zoneId);

        String accessToken = authService.getAccessToken();

        try {
            return callRegisterDevice(request, accessToken);
        } catch (WebClientResponseException ex) {
            if (!isInvalidTokenError(ex)) {
                throw ex;
            }

            String newAccessToken = authService.refreshAccessToken();
            return callRegisterDevice(request, newAccessToken);
        }
    }

    private Map<String, String> callRegisterDevice(Map<String, String> request, String accessToken) {
        Map response = webClient.post()
                .uri("/devices")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response;
    }

    private boolean isInvalidTokenError(WebClientResponseException ex) {
        if (ex.getStatusCode().value() == 401) {
            return true;
        }

        if (ex.getStatusCode().is4xxClientError()) {
            String responseBody = ex.getResponseBodyAsString();
            if (responseBody != null) {
                String normalizedBody = responseBody.toLowerCase();
                return normalizedBody.contains("invalid") && normalizedBody.contains("token");
            }
        }

        return false;
    }

}
