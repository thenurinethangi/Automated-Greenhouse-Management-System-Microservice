package com.agms.telemetryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ZoneDTO {

    private long id;
    private String zoneName;
    private double minTemp;
    private double maxTemp;
    private String deviceId;
    private String userEmail;

    public ZoneDTO(String zoneName, double minTemp, double maxTemp, String deviceId, String userEmail) {
        this.zoneName = zoneName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.deviceId = deviceId;
        this.userEmail = userEmail;
    }
}
