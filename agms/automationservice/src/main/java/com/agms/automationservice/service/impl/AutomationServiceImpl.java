package com.agms.automationservice.service.impl;

import java.util.Map;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.agms.automationservice.client.ZoneInterface;
import com.agms.automationservice.dto.RequestDTO;
import com.agms.automationservice.entity.Log;
import com.agms.automationservice.repository.AutomationRepository;
import com.agms.automationservice.service.AutomationService;
import com.agms.automationservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomationServiceImpl implements AutomationService {

    private final AutomationRepository automationRepository;
    private final ZoneInterface zoneInterface;

    public APIResponse processLogic(RequestDTO requestDTO) {

        ResponseEntity<APIResponse> zoneResponse = zoneInterface.getZoneById(requestDTO.getZoneId());
        Map<String, Object> response = null;

        if (zoneResponse != null && zoneResponse.getBody() != null) {
            response = (Map<String, Object>) zoneResponse.getBody().getData();
        }

        String action = "NO_ACTION";

        if (response != null) {
            double minTemp = (double) response.get("minTemp");
            double maxTemp = (double) response.get("maxTemp");

            if (requestDTO.getTemperature() > maxTemp) {
                action = "TURN_FAN_ON";
                System.out.println("TURN_FAN_ON");
            } else if (requestDTO.getTemperature() < minTemp) {
                action = "TURN_HEATER_ON";
                System.out.println("TURN_HEATER_ON");
            } else {
                action = "TEMPERATURE_OK";
                System.out.println("TEMPERATURE_OK");
            }
        }

        Log log = new Log();
        log.setZoneId(requestDTO.getZoneId());
        log.setTemperature(requestDTO.getTemperature());
        log.setHumidity(requestDTO.getHumidity());
        log.setActionTaken(action);
        log.setTimestamp(new Date());

        Log savedlog = automationRepository.save(log);
        System.out.println("Log saved with ID: " + savedlog.getId());

        return new APIResponse(200, "Automation processed successfully", savedlog);
    }

    @Override
    public APIResponse listAllTriggeredActions() {

        automationRepository.findAll().forEach(log -> {
            System.out.println("Log ID: " + log.getId() + ", Zone ID: " + log.getZoneId() + ", Action: "
                    + log.getActionTaken() + ", Timestamp: " + log.getTimestamp());
        });
        return new APIResponse(200, "Logs retrieved successfully", automationRepository.findAll());

    }
}
