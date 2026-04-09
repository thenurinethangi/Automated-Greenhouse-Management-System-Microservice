package com.agms.cropInventoryService.entity;

import java.util.Date;

import com.agms.cropInventoryService.util.CropStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cropName;

    @Column(nullable = false)
    private Long quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CropStatus status;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Date createdAt;

    public Crop(String cropName, Long quantity, CropStatus status, String userEmail, Date createdAt) {
        this.cropName = cropName;
        this.quantity = quantity;
        this.status = status;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
    }
}
