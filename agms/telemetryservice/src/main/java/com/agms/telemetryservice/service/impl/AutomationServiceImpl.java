package com.agms.telemetryservice.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.agms.telemetryservice.client.AutomationInterface;
import com.agms.telemetryservice.dto.RequestDTO;
import com.agms.telemetryservice.service.AutomationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutomationServiceImpl implements AutomationService {

    private final AutomationInterface automationInterface;

    @Override
    public Map<String, Object> callAutomationServiceToApplyLogic(RequestDTO requestDTO) {

        automationInterface.processLogic(requestDTO).getBody();
        return null;
    }

}
