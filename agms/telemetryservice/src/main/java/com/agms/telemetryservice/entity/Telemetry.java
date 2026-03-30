package com.agms.telemetryservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deviceId;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private double humidity;

    public Telemetry(String deviceId, double temperature, double humidity) {
        this.deviceId = deviceId;
        this.temperature = temperature;
        this.humidity = humidity;
    }
}
