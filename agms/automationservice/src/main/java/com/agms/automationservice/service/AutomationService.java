package com.agms.automationservice.service;

import com.agms.automationservice.dto.RequestDTO;
import com.agms.automationservice.util.APIResponse;

public interface AutomationService {

    public APIResponse processLogic(RequestDTO requestDTO);

    public APIResponse listAllTriggeredActions(String email);
    
}
