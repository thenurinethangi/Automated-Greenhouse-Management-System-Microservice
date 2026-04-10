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
        try {
            var response = zoneInterface.getZoneByUserEmail(email);
            if (response == null || response.getBody() == null) {
                return new APIResponse(400, "Failed to fetch zones", new ArrayList<>());
            }
            
            List<Map<String, Object>> allZones = (List<Map<String, Object>>) response.getBody()
                    .getData();

            List<Telemetry> latestTelemetryList = new ArrayList<>();
            for (Map<String, Object> map : allZones) {
                String deviceId = (String) map.get("deviceId");
                if (deviceId != null && !deviceId.isEmpty()) {
                    telemetryRepository.findAllByDeviceIdOrderByReadTimeDesc(deviceId).stream().findFirst()
                            .ifPresent(latestTelemetryList::add);
                }
            }

            return new APIResponse(200, "Successfully retrieved latest telemetry!", latestTelemetryList);
        } catch (Exception e) {
            e.printStackTrace();
            return new APIResponse(500, "Error: " + e.getMessage(), new ArrayList<>());
        }
    }

    @Override
    public void fetchAndProcessTelemetry() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            var response = zoneInterface.getAllZones();
            if (response == null || response.getBody() == null) {
                System.out.println("getAllZones response is null");
                return;
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.getBody()
                    .getData();

            if (dataList == null || dataList.isEmpty()) {
                System.out.println("No zones found in system");
                return;
            }

            System.out.println("Found " + dataList.size() + " zones, processing...");

            List<ZoneDTO> zones = dataList.stream()
                    .map(map -> objectMapper.convertValue(map, ZoneDTO.class))
                    .collect(Collectors.toList());

            for (ZoneDTO zone : zones) {
                String deviceId = zone.getDeviceId();
                System.out.println("Zone: " + zone.getZoneName() + ", DeviceId: " + deviceId);

                if (deviceId == null || deviceId.isEmpty()) {
                    System.out.println("  Skipped - no deviceId");
                    continue;
                }

                try {
                    SensorDataDTO telemetry = deviceService.getDeviceTelemetry(deviceId);
                    if (telemetry == null || telemetry.getValue() == null) {
                        System.out.println("  Skipped - no telemetry data from IoT");
                        continue;
                    }

                    System.out.println("  Saving telemetry - Temp: " + telemetry.getValue().getTemperature() + 
                                     ", Humidity: " + telemetry.getValue().getHumidity());

                    Telemetry t = new Telemetry(
                            deviceId,
                            telemetry.getValue().getTemperature(),
                            telemetry.getValue().getHumidity(),
                            LocalDateTime.now());

                    Telemetry savedTelemetry = telemetryRepository.save(t);
                    System.out.println("  Saved successfully - ID: " + savedTelemetry.getId());

                    RequestDTO requestDTO = new RequestDTO(zone.getId(), savedTelemetry.getTemperature(),
                            savedTelemetry.getHumidity());
                    automationService.callAutomationServiceToApplyLogic(requestDTO);
                } catch (Exception e) {
                    System.out.println("  Error processing zone: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("Error in fetchAndProcessTelemetry: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
