package com.agms.telemetryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDTO {

    private Long zoneId;
    private double temperature;
    private double humidity;
}
