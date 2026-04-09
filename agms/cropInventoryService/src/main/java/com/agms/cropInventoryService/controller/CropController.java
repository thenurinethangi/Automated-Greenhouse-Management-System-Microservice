package com.agms.cropInventoryService.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agms.cropInventoryService.dto.CropSaveDTO;
import com.agms.cropInventoryService.dto.CropStatusDTO;
import com.agms.cropInventoryService.service.CropService;
import com.agms.cropInventoryService.util.APIResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private static final Logger logger = LoggerFactory.getLogger(CropController.class);
    private final CropService cropService;

    @PostMapping
    public ResponseEntity<APIResponse> registerNewBatch(@Valid @RequestBody CropSaveDTO cropSaveDTO, @RequestHeader("User-Email") String email) {
        logger.info("Registering new crop batch: {}", cropSaveDTO.getCropName());
        APIResponse response = cropService.registerNewBatch(cropSaveDTO, email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<APIResponse> updateCropStatus(@PathVariable long id, @Valid @RequestBody CropStatusDTO cropStatusDTO, @RequestHeader("User-Email") String email) {
        logger.info("Updating crop status for ID: {} to {}", id, cropStatusDTO.getStatus());
        APIResponse response = cropService.updateCropStatus(id, cropStatusDTO, email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<APIResponse> viewCurrentInventory(@RequestHeader("User-Email") String email) {
        logger.info("Fetching current crop inventory");
        APIResponse response = cropService.viewCurrentInventory(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}