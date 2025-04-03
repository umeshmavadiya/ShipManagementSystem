package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.CrewEntity;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.CrewRepository;
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
class CrewServiceTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CrewRepository crewRepository;
    @Mock
    private ShipRepository shipRepository;

    @InjectMocks
    private CrewService crewService;

    private CrewDTO mockCrewDTO;
    private CrewEntity mockCrewEntity;
    private ShipDTO mockShipDTO;
    private ShipEntity mockShipEntity;

    @BeforeEach
    void setUp() {
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
        mockShipEntity = new ShipEntity(1L, "Titanic", "Cruise", 5000, "Active");
        mockCrewEntity = new CrewEntity(1L, "Umesh Mavadiya", "Master", mockShipEntity);
        mockCrewDTO = new CrewDTO(1L, "Umesh Mavadiya", "Master", null);
    }

    @Test
    void assignCrewToShipTest() {
        when(shipRepository.findById(1L)).thenReturn(Optional.ofNullable(mockShipEntity));
        when(modelMapper.map(mockShipEntity, ShipDTO.class)).thenReturn(mockShipDTO);

        when(modelMapper.map(mockCrewDTO, CrewEntity.class)).thenReturn(mockCrewEntity);

        when(crewRepository.save(any(CrewEntity.class))).thenReturn(mockCrewEntity);
        when(modelMapper.map(mockCrewEntity, CrewDTO.class)).thenReturn(mockCrewDTO);

        CrewDTO result = crewService.assignCrewToShip(1L, mockCrewDTO);
        assertNotNull(result);
        assertEquals("Umesh Mavadiya", result.getName());
        verify(crewRepository, times(1)).save(any(CrewEntity.class));
    }

    @Test
    void assignCrewToShip_ShouldThrowException_WhenShipNotFound() {
        Long shipId = 1L;
        when(shipRepository.findById(shipId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> crewService.assignCrewToShip(shipId, mockCrewDTO));
    }

    @Test
    void getCrewByShipTest() {
        List<CrewEntity> crewList = List.of(mockCrewEntity, mockCrewEntity);
        when(crewRepository.findAll()).thenReturn(crewList);
        when(modelMapper.map(mockCrewEntity, CrewDTO.class)).thenReturn(mockCrewDTO);
        when(modelMapper.map(mockCrewEntity, CrewDTO.class)).thenReturn(mockCrewDTO);
        List<CrewDTO> result = crewService.getCrewByShip(1L);
        assertEquals(2, result.size());
        verify(crewRepository, times(1)).findAll();
    }

    @Test
    void getCrewByShipIdAndCrewIdTest() {
        when(crewRepository.findCrewByShipIdAndCrewId(1L, 1L)).thenReturn(mockCrewEntity);
        when(modelMapper.map(mockCrewEntity, CrewDTO.class)).thenReturn(mockCrewDTO);

        CrewDTO result = crewService.getCrewByShipIdAndCrewId(1L, 1L);

        assertNotNull(result);
        assertEquals("Umesh Mavadiya", result.getName());
        verify(crewRepository, times(1)).findCrewByShipIdAndCrewId(1L, 1L);
    }

    @Test
    void updateCrewPositionTest() {

        CrewEntity updatedCrewEntity = mockCrewEntity;
        CrewDTO updatedCrewDTO = mockCrewDTO;

        when(crewRepository.findById(1L)).thenReturn(Optional.of(mockCrewEntity));
        updatedCrewEntity.setPosition("Engineer");
        when(crewRepository.save(updatedCrewEntity)).thenReturn(updatedCrewEntity);
        updatedCrewDTO.setPosition("Engineer");
        when(modelMapper.map(updatedCrewEntity, CrewDTO.class)).thenReturn(updatedCrewDTO);

        CrewDTO result = crewService.updateCrewPosition(1L, "Engineer");

        assertNotNull(result);
        assertEquals("Engineer", result.getPosition());
        verify(crewRepository, times(1)).save(updatedCrewEntity);
    }

    @Test
    void updateCrewPosition_ShouldThrowException_WhenCrewNotFound() {
        Long crewId = 1L;
        when(crewRepository.findById(crewId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> crewService.updateCrewPosition(crewId, "Engineer"));
    }


    @Test
    void removeCrewFromShip_ShouldRemoveCrew_WhenExists() {
        Long crewId = 1L;
        when(crewRepository.existsById(crewId)).thenReturn(true);
        doNothing().when(crewRepository).deleteById(crewId);

        crewService.removeCrewFromShip(crewId);

        verify(crewRepository, times(1)).deleteById(crewId);
    }

    @Test
    void removeCrewFromShip_ShouldThrowException_WhenCrewNotFound() {
        Long crewId = 1L;
        when(crewRepository.existsById(crewId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> crewService.removeCrewFromShip(crewId));
    }
}