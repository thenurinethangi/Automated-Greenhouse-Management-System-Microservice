package com.agms.zoneservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.agms.zoneservice.dto.DeviceDTO;
import com.agms.zoneservice.feign.IoTDeviceInterface;
import com.agms.zoneservice.service.IoTAuthService;
import com.agms.zoneservice.service.IoTDeviceService;

import feign.FeignException;

@Service
public class IoTDeviceServiceImpl implements IoTDeviceService {

    private final IoTAuthService authService;
    private final IoTDeviceInterface deviceInterface;

    public IoTDeviceServiceImpl(IoTAuthService authService, IoTDeviceInterface deviceInterface) {
        this.authService = authService;
        this.deviceInterface = deviceInterface;
    }

    @Override
    public Map<String, String> registerDevice(String name, String zoneId) {
        DeviceDTO request = new DeviceDTO();
        request.setName(name);
        request.setZoneId(zoneId);

        try {
            return callRegisterDevice(request);
        } catch (FeignException.Unauthorized ex) {
            authService.refreshAccessToken();
            return callRegisterDevice(request);
        }
    }

    private Map<String, String> callRegisterDevice(DeviceDTO request) {
        String token = authService.getAccessToken();
        DeviceDTO savedDevice = deviceInterface.saveDevice("Bearer " + token, request);
        Map<String, String> response = new HashMap<>();
        response.put("deviceId", savedDevice.getDeviceId());
        return response;
    }

}
