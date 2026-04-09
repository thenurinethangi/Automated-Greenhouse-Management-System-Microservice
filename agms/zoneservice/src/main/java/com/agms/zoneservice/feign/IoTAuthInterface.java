package com.agms.zoneservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.agms.zoneservice.dto.UserDTO;

@FeignClient(name = "IOT-SERVICE", path = "/api/auth", contextId = "iot-auth")
public interface IoTAuthInterface {

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO dto);

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO dto);

    @PostMapping("/refresh")
    public UserDTO refresh(@RequestBody UserDTO dto);
}