package com.example.ShipManagementSystem.repository;

import com.example.ShipManagementSystem.entity.CrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CrewRepository  extends JpaRepository<CrewEntity, Long> {
    @Query("SELECT c FROM CrewEntity c WHERE c.id = :crewId AND c.ship.id = :shipId")
    CrewEntity findCrewByShipIdAndCrewId(@Param("shipId") Long shipId, @Param("crewId") Long crewId);
}
