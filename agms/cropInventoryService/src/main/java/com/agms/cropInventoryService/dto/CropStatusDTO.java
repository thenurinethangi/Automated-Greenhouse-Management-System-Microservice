package com.agms.cropInventoryService.dto;

import com.agms.cropInventoryService.util.CropStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CropStatusDTO {

    @NotNull(message = "Status cannot be null")
    private CropStatus status;
}
