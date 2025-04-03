package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.RouteEntity;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.repository.RouteRepository;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private ShipRepository shipRepository;
    @InjectMocks
    private RouteService routeService;

    private ShipDTO mockShipDTO;
    private ShipEntity mockShipEntity;
    private RouteDTO mockRouteDTO;
    private RouteEntity mockRouteEntity;



    @BeforeEach
    void setUp() {
    }

    @Test
    void assignRouteToShip() {
    }

    @Test
    void getRoutesByShip() {
    }

    @Test
    void getRouteByShipIdAndRouteId() {
    }

    @Test
    void updateRoute() {
    }

    @Test
    void deleteRoute() {
    }
}