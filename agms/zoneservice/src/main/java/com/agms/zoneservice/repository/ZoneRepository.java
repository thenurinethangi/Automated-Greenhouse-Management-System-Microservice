package com.agms.zoneservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.agms.zoneservice.entity.Zone;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

}
