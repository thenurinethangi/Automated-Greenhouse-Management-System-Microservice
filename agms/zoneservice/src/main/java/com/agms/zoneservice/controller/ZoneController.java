package com.agms.zoneservice.controller;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agms.zoneservice.dto.ZoneDTO;
import com.agms.zoneservice.service.ZoneService;
import com.agms.zoneservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<APIResponse> createZone(@Valid @RequestBody ZoneDTO zoneDTO,
            @RequestHeader("User-Email") String email) {
        APIResponse response = zoneService.createZone(zoneDTO, email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<APIResponse> getAllZones() {
        APIResponse response = zoneService.getAllZones();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse> getZoneById(@PathVariable Long id) {
        APIResponse response = zoneService.getZoneById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<APIResponse> getZoneByUserEmail(@PathVariable String email) {
        APIResponse response = zoneService.getZoneByUserEmail(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse> updateThresholdsById(@PathVariable Long id, @RequestBody ZoneDTO zoneDTO,
            @RequestHeader("User-Email") String email) {
        APIResponse response = zoneService.updateThresholdsById(id, zoneDTO, email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse> deleteZoneById(@PathVariable Long id,
            @RequestHeader("User-Email") String email) {
        APIResponse response = zoneService.deleteZoneById(id, email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
