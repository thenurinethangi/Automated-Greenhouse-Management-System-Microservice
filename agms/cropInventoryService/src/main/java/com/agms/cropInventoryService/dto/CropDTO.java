package com.agms.cropInventoryService.dto;

import java.util.Date;

import com.agms.cropInventoryService.util.CropStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CropDTO {

    private Long id;
    private String cropName;
    private Long quantity;
    private CropStatus status;
    private Date createdAt;

    public CropDTO(Long id, String cropName, Long quantity, CropStatus status) {
        this.id = id;
        this.cropName = cropName;
        this.quantity = quantity;
        this.status = status;
    }
}
