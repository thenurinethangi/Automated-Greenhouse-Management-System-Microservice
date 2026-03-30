package com.agms.telemetryservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.agms.telemetryservice.service.IoTAuthService;
import com.agms.telemetryservice.service.IoTDeviceService;

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
    public Map<String, String> getDeviceTelemetry(String deviceId) {

        String accessToken = authService.getAccessToken();

        try {
            return callGetTelemetry(accessToken, deviceId);
        } catch (WebClientResponseException ex) {
            if (!isInvalidTokenError(ex)) {
                throw ex;
            }

            String newAccessToken = authService.refreshAccessToken();
            return callGetTelemetry(newAccessToken, deviceId);
        }
    }

    private Map<String, String> callGetTelemetry(String accessToken, String deviceId) {
        Map<String, String> response = webClient.post()
                .uri("/devices/telemetry/" + deviceId)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
            })
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
