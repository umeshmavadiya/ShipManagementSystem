package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipServiceTest {

    @Mock
    private ShipRepository shipRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ShipService shipService;

    private ShipEntity mockShipEntity;
    private ShipDTO mockShipDTO;


    @BeforeEach
    void setUp() {
        mockShipEntity = new ShipEntity(1L, "Titanic", "Cruise", 5000, "Active");
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
    }

    @Test
    void createNewShipTest()  throws Exception  {
        when(modelMapper.map(mockShipDTO, ShipEntity.class)).thenReturn(mockShipEntity);
        when(shipRepository.save(mockShipEntity)).thenReturn(mockShipEntity);
        when(modelMapper.map(mockShipEntity, ShipDTO.class)).thenReturn(mockShipDTO);

        ShipDTO result = shipService.createNewShip(mockShipDTO);

        assertNotNull(result);
        assertEquals(mockShipDTO.getName(), result.getName());
        verify(shipRepository, times(1)).save(mockShipEntity);
    }

    @Test
    void getShipDetailsById_ShouldThrowException_WhenShipNotFound() {
        when(shipRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> shipService.getShipDetailsById(2L));
        assertEquals("Ship not found with ID: 2", exception.getMessage());
    }

    @Test
    void getShipDetailsById() {
        when(shipRepository.findById(1L)).thenReturn(Optional.ofNullable(mockShipEntity));
        when(modelMapper.map(mockShipEntity,ShipDTO.class)).thenReturn(mockShipDTO);
        
        ShipDTO result = shipService.getShipDetailsById(1L);
        assertNotNull(result);
        assertEquals("Titanic", result.getName());
        verify(shipRepository, times(1)).findById(1L);
    }

    @Test
    void getAllShipDetails() {
        when(shipRepository.findAll()).thenReturn(List.of(mockShipEntity));
        when(modelMapper.map(mockShipEntity,ShipDTO.class)).thenReturn(mockShipDTO);

        List<ShipDTO> result = shipService.getAllShipDetails();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(shipRepository, times(1)).findAll();
    }

    @Test
    void updateShipDetailsById_ShouldUpdateAllFields_WhenAllFieldsProvided() {
        Long shipId = 1L;
        ShipEntity existingShip = new ShipEntity(shipId, "Titanic", "Cruise", 5000, "Active");
        ShipDTO updateRequest = new ShipDTO(null, "Updated Titanic", "Cargo", 5500, "Inactive");

        ShipEntity updatedShip = new ShipEntity(shipId, "Updated Titanic", "Cargo", 5500, "Inactive");
        ShipDTO updatedDTO = new ShipDTO(shipId, "Updated Titanic", "Cargo", 5500, "Inactive");

        when(shipRepository.findById(shipId)).thenReturn(Optional.of(existingShip));
        when(shipRepository.save(any(ShipEntity.class))).thenReturn(updatedShip);
        when(modelMapper.map(updatedShip, ShipDTO.class)).thenReturn(updatedDTO);

        ShipDTO result = shipService.updateShipDetailsById(shipId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Titanic", result.getName());
        assertEquals("Cargo", result.getType());
        assertEquals(5500, result.getCapacity());
        assertEquals("Inactive", result.getStatus());

        verify(shipRepository, times(1)).findById(shipId);
        verify(shipRepository, times(1)).save(any(ShipEntity.class));
    }

    @Test
    void updateShipDetailsById_ShouldReturnNull_WhenShipNotFound() {
        Long shipId = 1L;
        ShipDTO updateRequest = new ShipDTO(null, "Updated Titanic", "Cargo", 5500, "Inactive");

        when(shipRepository.findById(shipId)).thenReturn(Optional.empty());

        ShipDTO result = shipService.updateShipDetailsById(shipId, updateRequest);

        assertNull(result, "Expected result to be null when ship is not found");

        verify(shipRepository, times(1)).findById(shipId);
        verify(shipRepository, never()).save(any(ShipEntity.class));
    }
    @Test
    void updateShipDetailsById_ShouldUpdateOnlyProvidedFields_WhenSomeFieldsNull() {
        Long shipId = 1L;
        ShipEntity existingShip = new ShipEntity(shipId, "Titanic", "Cruise", 5000, "Active");

        ShipDTO updateRequest = new ShipDTO(null, null, null, null, null);

        ShipEntity updatedShip = new ShipEntity(shipId, "Updated Titanic", "Cruise", 5500, "Active");
        ShipDTO updatedDTO = new ShipDTO(shipId, "Updated Titanic", "Cruise", 5500, "Active");

        when(shipRepository.findById(shipId)).thenReturn(Optional.of(existingShip));
        when(shipRepository.save(any(ShipEntity.class))).thenReturn(updatedShip);
        when(modelMapper.map(updatedShip, ShipDTO.class)).thenReturn(updatedDTO);

        ShipDTO result = shipService.updateShipDetailsById(shipId, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Titanic", result.getName());
        assertEquals("Cruise", result.getType());
        assertEquals(5500, result.getCapacity());
        assertEquals("Active", result.getStatus());

        verify(shipRepository, times(1)).findById(shipId);
        verify(shipRepository, times(1)).save(any(ShipEntity.class));
    }

    @Test
    void deleteShipDetailById_ShouldDeleteShip_WhenShipExists() {
        when(shipRepository.existsById(1L)).thenReturn(true);
        doNothing().when(shipRepository).deleteById(1L);

        assertDoesNotThrow(() -> shipService.deleteShipDetailById(1L));
        verify(shipRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteShipDetailById_ShouldThrowException_WhenShipNotFound() {
        when(shipRepository.existsById(2L)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> shipService.deleteShipDetailById(2L));
        assertEquals("Ship not found with ID: 2", exception.getMessage());
    }
}