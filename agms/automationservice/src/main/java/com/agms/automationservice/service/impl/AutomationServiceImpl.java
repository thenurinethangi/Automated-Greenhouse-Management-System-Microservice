package com.agms.automationservice.service.impl;

import java.util.Map;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.agms.automationservice.dto.RequestDTO;
import com.agms.automationservice.entity.Log;
import com.agms.automationservice.repository.AutomationRepository;
import com.agms.automationservice.service.AutomationService;
import com.agms.automationservice.service.ZoneService;
import com.agms.automationservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomationServiceImpl implements AutomationService {

    private final ZoneService zoneService;
    private final AutomationRepository automationRepository;

    public APIResponse processLogic(RequestDTO requestDTO) {

        Map<String, Object> response = zoneService.getZoneData(requestDTO.getZoneId());

        if (response != null) {
            double minTemp = (double) response.get("minTemp");
            double maxTemp = (double) response.get("maxTemp");

            String action = "NO_ACTION";

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
        return new APIResponse(404, "zone not foound", null);
    }

    @Override
    public APIResponse listAllTriggeredActions() {

        automationRepository.findAll().forEach(log -> {
            System.out.println("Log ID: " + log.getId() + ", Zone ID: " + log.getZoneId() + ", Action: " + log.getActionTaken() + ", Timestamp: " + log.getTimestamp());
        });
        return new APIResponse(200, "Logs retrieved successfully", automationRepository.findAll());

    }
}
