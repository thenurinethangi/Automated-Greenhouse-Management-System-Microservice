package com.agms.zoneservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

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

    @DecimalMin(value = "-273.15", message = "minTemp must be greater than or equal to -273.15")
    private double minTemp;

    @DecimalMin(value = "-273.15", message = "maxTemp must be greater than or equal to -273.15")
    private double maxTemp;

    private String deviceId;

    public ZoneDTO(String zoneName, double minTemp, double maxTemp) {
        this.zoneName = zoneName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

     public ZoneDTO(double minTemp, double maxTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }
}
