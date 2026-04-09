package com.agms.zoneservice.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.agms.zoneservice.dto.ZoneDTO;
import com.agms.zoneservice.entity.Zone;
import com.agms.zoneservice.repository.ZoneRepository;
import com.agms.zoneservice.service.IoTDeviceService;
import com.agms.zoneservice.service.ZoneService;
import com.agms.zoneservice.util.APIResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private static final Logger log = LoggerFactory.getLogger(ZoneServiceImpl.class);

    private final ZoneRepository zoneRepository;
    private final IoTDeviceService deviceService;

    public APIResponse createZone(ZoneDTO zoneDTO) {

        if (zoneDTO.getMinTemp() >= zoneDTO.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be smaller than maxTemp");
        }

        Map<String, String> response = deviceService.registerDevice(zoneDTO.getZoneName() + "Sensor", zoneDTO.getZoneName());
        log.info("IoT device registration response received for zoneName={}", zoneDTO.getZoneName());

        Zone zone = new Zone(zoneDTO.getZoneName(), zoneDTO.getMinTemp(), zoneDTO.getMaxTemp(),
                response.get("deviceId"));
        Zone savedZone = zoneRepository.save(zone);

        return new APIResponse(200, "successfully create a new zone", savedZone);
    }

    public APIResponse getAllZones() {

        List<Zone> zones = zoneRepository.findAll();
        return new APIResponse(200, "successfully retrieved all zones", zones);
    }

    public APIResponse getZoneById(Long id) {

        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone == null) {
            return new APIResponse(404, "zone not found", null);
        }
        return new APIResponse(200, "successfully retrieved the zone", zone);
    }

    public APIResponse updateThresholdsById(Long id, ZoneDTO zoneDTO) {

        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone == null) {
            return new APIResponse(404, "zone not found", null);
        }
        if (zoneDTO.getMinTemp() >= zoneDTO.getMaxTemp()) {
            throw new IllegalArgumentException("minTemp must be smaller than maxTemp");
        }

        zone.setMinTemp(zoneDTO.getMinTemp());
        zone.setMaxTemp(zoneDTO.getMaxTemp());
        Zone updatedZone = zoneRepository.save(zone);

        return new APIResponse(200, "successfully update the zone's thresholds", updatedZone);
    }

    public APIResponse deleteZoneById(Long id) {

        Zone zone = zoneRepository.findById(id).orElse(null);
        if (zone == null) {
            return new APIResponse(404, "zone not found", null);
        }

        zoneRepository.delete(zone);

        return new APIResponse(200, "successfully deleted the zone", null);
    }

}
