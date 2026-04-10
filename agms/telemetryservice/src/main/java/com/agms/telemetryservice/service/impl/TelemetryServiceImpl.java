package com.agms.telemetryservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agms.telemetryservice.client.ZoneInterface;
import com.agms.telemetryservice.dto.RequestDTO;
import com.agms.telemetryservice.dto.SensorDataDTO;
import com.agms.telemetryservice.dto.ZoneDTO;
import com.agms.telemetryservice.entity.Telemetry;
import com.agms.telemetryservice.repository.TelemetryRepository;
import com.agms.telemetryservice.service.IoTAuthService;
import com.agms.telemetryservice.service.IoTDeviceService;
import com.agms.telemetryservice.service.TelemetryService;
import com.agms.telemetryservice.util.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TelemetryServiceImpl implements TelemetryService {

    @SuppressWarnings("unused")
    private final IoTAuthService authService;
    private final IoTDeviceService deviceService;
    private final TelemetryRepository telemetryRepository;
    private final AutomationServiceImpl automationService;

    private final ZoneInterface zoneInterface;

    @Override
    public APIResponse fetchLatest() {

        // List<Map<String, Object>> allZones = zoneService.getAllZones();

        // for (Map<String, Object> map : allZones) {
        // String deviceId = map.get("deviceId").toString();
        // Map<String, String> telemetry = deviceService.getDeviceTelemetry(deviceId);

        // Telemetry savedTelemetry = telemetryRepository.save(new Telemetry(
        // deviceId,
        // Double.parseDouble(telemetry.get("temperature")),
        // Double.parseDouble(telemetry.get("humidity"))));

        // Map<String, Object> data = Map.of(
        // "zoneId", map.get("zoneId"),
        // "temperature", savedTelemetry.getTemperature(),
        // "humidity", savedTelemetry.getHumidity());
        // automationService.callAutomationServiceToApplyLogic(data);

        // }

        return null;
    }

    @Override
    public void fetchAndProcessTelemetry() {

        ObjectMapper objectMapper = new ObjectMapper();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) zoneInterface.getAllZones().getBody()
                .getData();

        List<ZoneDTO> zones = dataList.stream()
                .map(map -> objectMapper.convertValue(map, ZoneDTO.class))
                .collect(Collectors.toList());

        for (ZoneDTO zone : zones) {
            System.out.println(zone.getZoneName() + " ++++++++++ " + zone.getDeviceId());
            String deviceId = zone.getDeviceId();
            SensorDataDTO telemetry = deviceService.getDeviceTelemetry(deviceId);

            Telemetry t = new Telemetry(
                    deviceId,
                    telemetry.getValue().getTemperature(),
                    telemetry.getValue().getHumidity(),
                    LocalDateTime.now());

            Telemetry savedTelemetry = telemetryRepository.save(t);

            RequestDTO requestDTO = new RequestDTO(zone.getId(), savedTelemetry.getTemperature(), savedTelemetry.getHumidity());
            automationService.callAutomationServiceToApplyLogic(requestDTO);
        }
    }
}
