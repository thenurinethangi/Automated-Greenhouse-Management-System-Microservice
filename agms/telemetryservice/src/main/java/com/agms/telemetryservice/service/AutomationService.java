package com.agms.telemetryservice.service;

import java.util.Map;

import com.agms.telemetryservice.dto.RequestDTO;

public interface AutomationService {

    public Map<String, Object> callAutomationServiceToApplyLogic(RequestDTO requestDTO);
}
