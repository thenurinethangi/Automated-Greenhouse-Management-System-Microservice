package com.agms.cropInventoryService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agms.cropInventoryService.entity.Crop;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

}
