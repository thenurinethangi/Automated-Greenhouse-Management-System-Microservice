package com.agms.automationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agms.automationservice.entity.Log;

@Repository
public interface AutomationRepository extends JpaRepository<Log, Long> {

    public Log findFirstByZoneIdOrderByTimestampDesc(Long zoneId);
    
}
