package com.agms.zoneservice.service;

public interface IoTAuthService {

    public String getAccessToken();

    public String refreshAccessToken();
}
