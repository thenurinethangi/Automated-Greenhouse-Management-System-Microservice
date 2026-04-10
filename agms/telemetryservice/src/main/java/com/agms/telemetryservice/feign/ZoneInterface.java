package com.agms.telemetryservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.agms.telemetryservice.util.APIResponse;

@FeignClient(name = "ZONESERVICE", path = "/api/zones", contextId = "zone-service")
public interface ZoneInterface {

    @GetMapping
    public ResponseEntity<APIResponse> getAllZones(@RequestHeader("User-Email") String email);
}
