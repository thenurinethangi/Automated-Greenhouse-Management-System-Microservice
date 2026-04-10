package com.agms.automationservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agms.automationservice.dto.RequestDTO;
import com.agms.automationservice.service.AutomationService;
import com.agms.automationservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/automation")
@RequiredArgsConstructor
public class AutomationController {

    private final AutomationService automationService;

    @PostMapping("/process")
    public ResponseEntity<APIResponse> processLogic(@RequestBody RequestDTO requestDTO) {

        APIResponse response = automationService.processLogic(requestDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/logs")
    public ResponseEntity<APIResponse> listAllTriggeredActions(@RequestHeader("User-Email") String email) {

        APIResponse response = automationService.listAllTriggeredActions(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
