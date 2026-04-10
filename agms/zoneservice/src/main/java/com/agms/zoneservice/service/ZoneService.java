package com.agms.zoneservice.service;

import com.agms.zoneservice.dto.ZoneDTO;
import com.agms.zoneservice.util.APIResponse;

public interface ZoneService {

    public APIResponse createZone(ZoneDTO zoneDTO, String email);

    public APIResponse getAllZones();

    public APIResponse getZoneById(Long id);

    public APIResponse getZoneByUserEmail(String email);

    public APIResponse updateThresholdsById(Long id, ZoneDTO zoneDTO, String email);

    public APIResponse deleteZoneById(Long id, String email);

}
