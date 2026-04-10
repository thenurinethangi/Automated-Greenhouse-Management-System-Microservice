package com.agms.telemetryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValueDTO {
    private double temperature;
    private Unit tempUnit;

    private double humidity;
    private Unit humidityUnit;
}
