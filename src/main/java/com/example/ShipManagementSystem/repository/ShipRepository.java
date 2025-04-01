package com.example.ShipManagementSystem.repository;

import com.example.ShipManagementSystem.entity.ShipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ShipRepository  extends JpaRepository<ShipEntity, Long> {

}
