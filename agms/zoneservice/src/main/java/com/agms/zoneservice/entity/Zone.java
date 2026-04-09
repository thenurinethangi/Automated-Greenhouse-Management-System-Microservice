package com.agms.zoneservice.entity;

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
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String zoneName;

    @Column(nullable = false)
    private double minTemp;

    @Column(nullable = false)
    private double maxTemp;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String userEmail;

    public Zone(String zoneName, double minTemp, double maxTemp, String deviceId, String userEmail) {
        this.zoneName = zoneName;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.deviceId = deviceId;
        this.userEmail = userEmail;
    }
}
