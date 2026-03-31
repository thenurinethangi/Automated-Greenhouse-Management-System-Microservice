package com.agms.telemetryservice.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.agms.telemetryservice.entity.Telemetry;
import com.agms.telemetryservice.repository.TelemetryRepository;
import com.agms.telemetryservice.service.IoTAuthService;
import com.agms.telemetryservice.service.IoTDeviceService;
import com.agms.telemetryservice.service.TelemetryService;
import com.agms.telemetryservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TelemetryServiceImpl implements TelemetryService {

    @SuppressWarnings("unused")
    private final IoTAuthService authService;
    private final IoTDeviceService deviceService;
    private final ZoneServiceImpl zoneService;
    private final TelemetryRepository telemetryRepository;
    private final AutomationServiceImpl automationService;

    @Override
    public APIResponse fetchLatest() {

        List<Map<String, Object>> allZones = zoneService.getAllZones();

        for (Map<String, Object> map : allZones) {
            String deviceId = map.get("deviceId").toString();
            Map<String, String> telemetry = deviceService.getDeviceTelemetry(deviceId);

            Telemetry savedTelemetry = telemetryRepository.save(new Telemetry(
                    deviceId,
                    Double.parseDouble(telemetry.get("temperature")),
                    Double.parseDouble(telemetry.get("humidity"))));

            Map<String, Object> data = Map.of(
                    "zoneId", map.get("zoneId"),
                    "temperature", savedTelemetry.getTemperature(),
                    "humidity", savedTelemetry.getHumidity());
            automationService.callAutomationServiceToApplyLogic(data);

        }

        return null;
    }
}
