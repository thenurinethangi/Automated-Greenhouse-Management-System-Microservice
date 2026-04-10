package com.agms.telemetryservice.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public APIResponse fetchLatest(String email) {

        List<Map<String, Object>> allZones = (List<Map<String, Object>>) zoneInterface.getZoneByUserEmail(email).getBody()
                .getData();

        List<Telemetry> latestTelemetryList = new ArrayList<>();
        for (Map<String, Object> map : allZones) {
            String deviceId = (String) map.get("deviceId");
            telemetryRepository.findAllByDeviceIdOrderByReadTimeDesc(deviceId).stream().findFirst()
                    .ifPresent(latestTelemetryList::add);
        }

        return new APIResponse(200, "Successfully retrieved latest telemetry!", latestTelemetryList);
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

            RequestDTO requestDTO = new RequestDTO(zone.getId(), savedTelemetry.getTemperature(),
                    savedTelemetry.getHumidity());
            automationService.callAutomationServiceToApplyLogic(requestDTO);
        }
    }
}
