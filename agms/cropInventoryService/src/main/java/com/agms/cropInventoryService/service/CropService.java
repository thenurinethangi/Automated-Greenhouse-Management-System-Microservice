package com.agms.cropInventoryService.service;

import com.agms.cropInventoryService.dto.CropSaveDTO;
import com.agms.cropInventoryService.dto.CropStatusDTO;
import com.agms.cropInventoryService.util.APIResponse;

public interface CropService {

    public APIResponse registerNewBatch(CropSaveDTO cropSaveDTO);

    public APIResponse updateCropStatus(long id, CropStatusDTO cropStatusDTO);

    public APIResponse viewCurrentInventory();
}
