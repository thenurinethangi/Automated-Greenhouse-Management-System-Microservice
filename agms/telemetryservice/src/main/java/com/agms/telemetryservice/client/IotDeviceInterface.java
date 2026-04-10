package com.agms.telemetryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.agms.telemetryservice.dto.SensorDataDTO;

@FeignClient(name = "IOT-SERVICE", path = "/api/devices", contextId = "iot-device")
public interface IotDeviceInterface {

    @GetMapping("/telemetry/{deviceId}")
    public SensorDataDTO readData(@RequestHeader("Authorization") String token, @PathVariable String deviceId);
}
