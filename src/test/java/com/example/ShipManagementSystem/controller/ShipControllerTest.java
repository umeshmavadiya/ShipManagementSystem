package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.service.ShipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipControllerTest {

    @Mock
    private ShipService shipService;

    @InjectMocks
    private ShipController shipController;

    private ShipDTO mockShipDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
    }

    //  Test Get All Ships
    @Test
    void getAllShipsDetailsTest() {
        List<ShipDTO> shipList = Arrays.asList(mockShipDTO);
        when(shipService.getAllShipDetails()).thenReturn(shipList);

        ResponseEntity<ApiResponse<List<ShipDTO>>> response = shipController.getAllShipsDetails();

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(shipService, times(1)).getAllShipDetails();
    }

    // Test Create New Ship
    @Test
    void createNewShipTest() {
        when(shipService.createNewShip(any(ShipDTO.class))).thenReturn(mockShipDTO);

        ResponseEntity<ApiResponse<ShipDTO>> response = shipController.createNewShip(mockShipDTO);

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Titanic", response.getBody().getData().getName());
        verify(shipService, times(1)).createNewShip(any(ShipDTO.class));
    }

    // Test Get Ship by ID
    @Test
    void getShipDetailsByIdTest() {
        when(shipService.getShipDetailsById(1L)).thenReturn(mockShipDTO);

        ResponseEntity<ApiResponse<ShipDTO>> response = shipController.getShipDetailsById(1L);

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals(1L, response.getBody().getData().getShipId());
        verify(shipService, times(1)).getShipDetailsById(1L);
    }

    @Test
    void updateShipDetailByIdTest() {
        when(shipService.updateShipDetailsById(eq(1L), any(ShipDTO.class))).thenReturn(mockShipDTO);

        ResponseEntity<ApiResponse<ShipDTO>> response = shipController.updateShipDetailById(1L, mockShipDTO);

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Titanic", response.getBody().getData().getName());
        verify(shipService, times(1)).updateShipDetailsById(eq(1L), any(ShipDTO.class));
    }

    // Test Delete Ship
    @Test
    void deleteShipDetailByIdTest() {
        doNothing().when(shipService).deleteShipDetailById(1L);

        ResponseEntity<ApiResponse<String>> response = shipController.deleteShipDetailById(1L);

        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getStatusCode());
        assertEquals("Ship deleted successfully", response.getBody().getMessage());
        verify(shipService, times(1)).deleteShipDetailById(1L);
    }
}