package com.agms.telemetryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agms.telemetryservice.service.TelemetryService;
import com.agms.telemetryservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sensors/")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @GetMapping("/latest")
    public ResponseEntity<APIResponse> fetchLatest() {

        APIResponse response = telemetryService.fetchLatest();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
