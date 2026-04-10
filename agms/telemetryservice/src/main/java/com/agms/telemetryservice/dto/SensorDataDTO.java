package com.agms.telemetryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SensorDataDTO {
    private String deviceId;
    private String zoneId;
    private ValueDTO value;
    private String capturedAt;
}
