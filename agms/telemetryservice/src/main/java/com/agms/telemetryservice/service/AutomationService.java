package com.agms.telemetryservice.service;

import java.util.Map;

public interface AutomationService {

    public Map<String, Object> callAutomationServiceToApplyLogic(Map<String, Object> data);
}
