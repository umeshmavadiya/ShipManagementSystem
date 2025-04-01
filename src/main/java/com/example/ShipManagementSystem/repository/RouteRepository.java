package com.example.ShipManagementSystem.repository;

import com.example.ShipManagementSystem.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository  extends JpaRepository<RouteEntity, Long> {
    @Query("SELECT c FROM RouteEntity c WHERE c.id = :routeId AND c.ship.id = :shipId")
    RouteEntity findRouteByShipIdAndRouteId(@Param("shipId") Long shipId, @Param("routeId") Long routeId);
}
