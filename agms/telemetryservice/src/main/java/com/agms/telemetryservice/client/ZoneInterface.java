package com.agms.telemetryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.agms.telemetryservice.util.APIResponse;

@FeignClient(name = "ZONESERVICE", path = "/api/zones", contextId = "zone-service")
public interface ZoneInterface {

    @GetMapping
    public ResponseEntity<APIResponse> getAllZones();
}
