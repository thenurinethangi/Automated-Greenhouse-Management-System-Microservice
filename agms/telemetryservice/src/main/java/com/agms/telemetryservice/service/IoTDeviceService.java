package com.agms.telemetryservice.service;

import com.agms.telemetryservice.dto.SensorDataDTO;

public interface IoTDeviceService {
    
    public SensorDataDTO getDeviceTelemetry(String deviceId);
}
