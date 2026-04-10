package com.agms.zoneservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.agms.zoneservice.dto.UserDTO;
import com.agms.zoneservice.client.IoTAuthInterface;
import com.agms.zoneservice.service.IoTAuthService;

@Service
public class IoTAuthServiceImpl implements IoTAuthService {

    private static final Logger log = LoggerFactory.getLogger(IoTAuthServiceImpl.class);

    private String accessToken;
    private String refreshToken;

    @Value("${iot.username}")
    private String username;

    @Value("${iot.password}")
    private String password;

    private final IoTAuthInterface authInterface;

    public IoTAuthServiceImpl(IoTAuthInterface authInterface) {
        this.authInterface = authInterface;
    }

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

        UserDTO request = new UserDTO();
        request.setRefreshToken(refreshToken);

        UserDTO response = authInterface.refresh(request);

        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();

        return this.accessToken;
    }

    private void login() {
        UserDTO request = new UserDTO();
        request.setUsername(username);
        request.setPassword(password);

        log.info("Logging in to IoT service with username: {}", username);
        UserDTO response = authInterface.login(request);

        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();
        log.info("Successfully logged in to IoT service and obtained tokens.");
    }
}
