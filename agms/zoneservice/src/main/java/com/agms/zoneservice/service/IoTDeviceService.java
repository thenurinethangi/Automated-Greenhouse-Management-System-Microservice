package com.agms.zoneservice.service;

import java.util.Map;

public interface IoTDeviceService {
    
    public Map<String, String> registerDevice(String name, String zoneId);
}
