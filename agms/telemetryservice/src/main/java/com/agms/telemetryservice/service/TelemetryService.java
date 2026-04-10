package com.agms.telemetryservice.service;

import com.agms.telemetryservice.util.APIResponse;

public interface TelemetryService {
    
    public APIResponse fetchLatest(String email);

    public void fetchAndProcessTelemetry();
}
