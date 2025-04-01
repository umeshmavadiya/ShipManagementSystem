package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.service.CrewService;
import com.example.ShipManagementSystem.service.ShipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrewControllerTest {
    @Mock
    private ShipService shipService;
    @Mock
    private CrewService crewService;

    @InjectMocks
    private CrewController crewController;

    private ShipDTO mockShipDTO;
    private CrewDTO crewDTO;
    private CrewDTO crewDTOResponce;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
        crewDTO = new CrewDTO(1L, "Titanic", "Cruise", new ShipDTO());
        crewDTOResponce = new CrewDTO(1L, "Titanic", "Cruise", mockShipDTO);
    }

    @Test
    void assignCrewToShipTest() throws Exception {
        Long shipId = 1L;
        when(crewService.assignCrewToShip(shipId,crewDTO)).thenReturn(crewDTOResponce);

        ResponseEntity<ApiResponse<CrewDTO>> response = crewController.assignCrewToShip(shipId,crewDTO);
        this.testPrintMockCrewDTOAsJson(response.getBody());
    }

    void testPrintMockCrewDTOAsJson(ApiResponse<CrewDTO> mockData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mockData);

        System.out.println("Mock Crew DTO (JSON): \n" + json);
    }

}