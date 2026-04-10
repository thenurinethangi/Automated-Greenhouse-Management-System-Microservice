package com.agms.automationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.agms.automationservice.util.APIResponse;

@FeignClient(name = "ZONESERVICE", path = "/api/zones", contextId = "zone-service")
public interface ZoneInterface {

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getZoneById(@PathVariable Long id);
}
