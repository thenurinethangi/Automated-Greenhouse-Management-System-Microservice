package com.agms.telemetryservice.service;

import java.util.Map;

public interface IoTDeviceService {
    
    public Map<String, String> getDeviceTelemetry(String deviceId);
}
