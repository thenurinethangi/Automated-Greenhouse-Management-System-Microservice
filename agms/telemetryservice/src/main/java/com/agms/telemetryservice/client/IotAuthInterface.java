package com.agms.telemetryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.agms.telemetryservice.dto.UserDTO;

@FeignClient(name = "IOT-SERVICE", path = "/api/auth", contextId = "iot-auth")
public interface IotAuthInterface {

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO dto);

    @PostMapping("/login")
    public UserDTO login(@RequestBody UserDTO dto);

    @PostMapping("/refresh")
    public UserDTO refresh(@RequestBody UserDTO dto);
}
