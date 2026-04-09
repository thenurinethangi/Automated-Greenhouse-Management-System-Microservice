package com.agms.zoneservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agms.zoneservice.entity.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    List<Zone> findAllByUserEmail(String userEmail);
}
