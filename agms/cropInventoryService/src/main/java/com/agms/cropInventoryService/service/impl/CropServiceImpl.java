package com.agms.cropInventoryService.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agms.cropInventoryService.dto.CropDTO;
import com.agms.cropInventoryService.dto.CropSaveDTO;
import com.agms.cropInventoryService.dto.CropStatusDTO;
import com.agms.cropInventoryService.entity.Crop;
import com.agms.cropInventoryService.exception.ResourceNotFoundException;
import com.agms.cropInventoryService.repository.CropRepository;
import com.agms.cropInventoryService.service.CropService;
import com.agms.cropInventoryService.util.APIResponse;
import com.agms.cropInventoryService.util.CropStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CropServiceImpl implements CropService {

    private static final Logger logger = LoggerFactory.getLogger(CropServiceImpl.class);
    private final CropRepository cropRepository;

    @Override
    public APIResponse registerNewBatch(CropSaveDTO cropSaveDTO, String email) {
        logger.info("Registering new crop batch with name: {} and quantity: {}", cropSaveDTO.getCropName(),
                cropSaveDTO.getQuantity());

        Crop crop = new Crop(cropSaveDTO.getCropName(), cropSaveDTO.getQuantity(), CropStatus.SEEDLING, email,
                new Date());
        Crop savedCrop = cropRepository.save(crop);

        logger.info("Crop batch registered successfully with ID: {}", savedCrop.getId());
        return new APIResponse(201, "Crop batch registered successfully", savedCrop);
    }

    @Override
    public APIResponse updateCropStatus(long id, CropStatusDTO cropStatusDTO, String email) {
        logger.info("Updating crop status for ID: {} to {}", id, cropStatusDTO.getStatus());

        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Crop batch not found with ID: {}", id);
                    return new ResourceNotFoundException("Crop batch not found with id: " + id);
                });

        if (crop.getUserEmail() != null && !crop.getUserEmail().equals(email)) {
            String message = "Unauthorized: You do not have permission to update this crop batch";
            logger.warn(message);
            return new APIResponse(403, message, null);

        }

        if (!isValidStateTransition(crop.getStatus(), cropStatusDTO.getStatus())) {
            String message = "Invalid state transition from " + crop.getStatus() + " to " + cropStatusDTO.getStatus();
            logger.warn(message);
            return new APIResponse(400, message, null);
        }

        crop.setStatus(cropStatusDTO.getStatus());
        Crop updatedCrop = cropRepository.save(crop);

        logger.info("Crop status updated successfully for ID: {}", id);
        return new APIResponse(200, "Crop status updated successfully", updatedCrop);
    }

    @Override
    @Transactional(readOnly = true)
    public APIResponse viewCurrentInventory(String email) {
        logger.info("Fetching current crop inventory");

        List<CropDTO> list = cropRepository.findAllByUserEmail(email).stream()
                .map(crop -> new CropDTO(crop.getId(), crop.getCropName(), crop.getQuantity(), crop.getStatus(),
                        crop.getCreatedAt()))
                .toList();

        logger.info("Retrieved {} crop batches from inventory", list.size());
        return new APIResponse(200, "Current crop inventory retrieved successfully", list);
    }

    private boolean isValidStateTransition(CropStatus currentStatus, CropStatus newStatus) {
        if (currentStatus == newStatus) {
            return true;
        }

        return switch (currentStatus) {
            case SEEDLING -> newStatus == CropStatus.VEGETATIVE;
            case VEGETATIVE -> newStatus == CropStatus.HARVESTED;
            case HARVESTED -> false;
        };
    }
}
