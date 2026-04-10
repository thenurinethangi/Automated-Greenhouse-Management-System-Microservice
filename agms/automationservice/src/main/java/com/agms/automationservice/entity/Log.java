package com.agms.automationservice.entity;

import java.util.Date;

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
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long zoneId;

    @Column(nullable = false)
    private double temperature;

    @Column(nullable = false)
    private double humidity;

    @Column(nullable = false)
    private String actionTaken;

    @Column(nullable = false)
    private Date timestamp;

    public Log(Long zoneId, double temperature, double humidity, String actionTaken) {
        this.zoneId = zoneId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.actionTaken = actionTaken;
        this.timestamp = new Date();
    }
}
