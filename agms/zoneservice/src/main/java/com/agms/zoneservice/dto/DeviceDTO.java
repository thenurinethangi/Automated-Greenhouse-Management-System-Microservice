package com.agms.zoneservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDTO {
    private String deviceId;
    private String name;
    private String zoneId;
    private String userId;
    private String createAt;
}
