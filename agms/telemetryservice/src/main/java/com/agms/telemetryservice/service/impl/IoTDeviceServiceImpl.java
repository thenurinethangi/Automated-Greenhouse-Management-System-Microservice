package com.agms.telemetryservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.agms.telemetryservice.client.IotDeviceInterface;
import com.agms.telemetryservice.dto.SensorDataDTO;
import com.agms.telemetryservice.service.IoTAuthService;
import com.agms.telemetryservice.service.IoTDeviceService;

@Service
public class IoTDeviceServiceImpl implements IoTDeviceService {

    private final IoTAuthService authService;
    private final IotDeviceInterface iotDeviceInterface;

    public IoTDeviceServiceImpl(IoTAuthService authService, IotDeviceInterface iotDeviceInterface) {
        this.authService = authService;
        this.iotDeviceInterface = iotDeviceInterface;
    }

    @Override
    public SensorDataDTO getDeviceTelemetry(String deviceId) {

        String accessToken = authService.getAccessToken();
        System.out.println("Access Token: " + accessToken);

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

    private SensorDataDTO callGetTelemetry(String accessToken, String deviceId) {

        SensorDataDTO sensorDataDTO = iotDeviceInterface.readData("Bearer " + accessToken, deviceId);
        System.out.println(sensorDataDTO.getCapturedAt()+" "+sensorDataDTO.getValue().getTemperature()+" "+sensorDataDTO.getValue().getHumidity());
        return sensorDataDTO;
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
