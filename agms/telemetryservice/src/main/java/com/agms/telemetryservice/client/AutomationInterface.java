package com.agms.telemetryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.agms.telemetryservice.dto.RequestDTO;
import com.agms.telemetryservice.util.APIResponse;

@FeignClient(name = "AUTOMATIONSERVICE", path = "/api/automation", contextId = "automation-service")
public interface AutomationInterface {

    @PostMapping("/process")
    public ResponseEntity<APIResponse> processLogic(@RequestBody RequestDTO requestDTO);
}
