package com.agms.telemetryservice.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agms.telemetryservice.client.IotAuthInterface;
import com.agms.telemetryservice.dto.UserDTO;
import com.agms.telemetryservice.service.IoTAuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IoTAuthServiceImpl implements IoTAuthService {

    private String accessToken;
    private String refreshToken;

    @Value("${iot.username}")
    private String username;

    @Value("${iot.password}")
    private String password;

    private final IotAuthInterface iotAuthInterface;

    @Override
    public String getAccessToken() {
        if (accessToken == null) {
            login();
        }
        return accessToken;
    }

    @Override
    public String refreshAccessToken() {
        if (refreshToken == null) {
            login();
            return accessToken;
        }

        UserDTO requestbody = new UserDTO();
        requestbody.setRefreshToken(refreshToken);

        UserDTO user = iotAuthInterface.refresh(requestbody);

        this.accessToken = user.getAccessToken();

        Object newRefreshToken = user.getRefreshToken();
        if (newRefreshToken instanceof String) {
            this.refreshToken = (String) newRefreshToken;
        }

        return this.accessToken;
    }

    private void login() {

        UserDTO requestbody = new UserDTO();
        requestbody.setUsername(username);
        requestbody.setPassword(password);

        UserDTO response = iotAuthInterface.login(requestbody);

        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();
    }
}
