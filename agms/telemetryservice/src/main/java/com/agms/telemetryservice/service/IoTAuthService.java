package com.agms.telemetryservice.service;

public interface IoTAuthService {
    
    public String getAccessToken();

    public String refreshAccessToken();
}
