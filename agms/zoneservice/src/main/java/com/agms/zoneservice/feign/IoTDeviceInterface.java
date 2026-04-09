package com.agms.zoneservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.agms.zoneservice.dto.DeviceDTO;

@FeignClient(name = "IOT-SERVICE", path = "/api/devices", contextId = "iot-device")
public interface IoTDeviceInterface {

    @PostMapping
    public DeviceDTO saveDevice(@RequestHeader("Authorization") String token, @RequestBody DeviceDTO device);
    
}
