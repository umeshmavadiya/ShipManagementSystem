package com.example.ShipManagementSystem.service;

import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.entity.CrewEntity;
import com.example.ShipManagementSystem.entity.RouteEntity;
import com.example.ShipManagementSystem.entity.ShipEntity;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.repository.RouteRepository;
import com.example.ShipManagementSystem.repository.ShipRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
        mockShipEntity = new ShipEntity(1L, "Titanic", "Cruise", 5000, "Active");
        mockRouteDTO = new RouteDTO(10L, mockShipDTO, "NYC -> LA" ,500.5 ,72.5, LocalDateTime.now(),LocalDateTime.now());
        mockRouteEntity = new RouteEntity(10L, mockShipEntity, "NYC -> LA" ,500.5 ,72.5, LocalDateTime.now(),LocalDateTime.now());
    }

    @Test
    void assignRouteToShipTest() {
        when(shipRepository.findById(1L)).thenReturn(Optional.ofNullable(mockShipEntity));
        when(modelMapper.map(mockShipEntity, ShipDTO.class)).thenReturn(mockShipDTO);

        when(modelMapper.map(mockRouteDTO, RouteEntity.class)).thenReturn(mockRouteEntity);
        when(routeRepository.save(any(RouteEntity.class))).thenReturn(mockRouteEntity);

        when(modelMapper.map(mockRouteEntity, RouteDTO.class)).thenReturn(mockRouteDTO);

        RouteDTO result = routeService.assignRouteToShip(1L,mockRouteDTO);
        assertNotNull(result);
        assertEquals("NYC -> LA", result.getPorts());
        verify(routeRepository, times(1)).save(any(RouteEntity.class));
    }
    @Test
    void assignRouteToShip_ShouldThrowException_WhenShipNotFound() {
        Long shipId = 1L;
        when(shipRepository.findById(shipId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> routeService.assignRouteToShip(shipId, mockRouteDTO));
    }

    @Test
    void getRoutesByShipTest() {
        List<RouteEntity> routeList = List.of(mockRouteEntity,mockRouteEntity);
        when(routeRepository.findAll()).thenReturn(routeList);
        when(modelMapper.map(mockRouteEntity, RouteDTO.class)).thenReturn(mockRouteDTO);
        when(modelMapper.map(mockRouteEntity, RouteDTO.class)).thenReturn(mockRouteDTO);
        List<RouteDTO> result = routeService.getRoutesByShip(1L);
        assertEquals(2, result.size());
        verify(routeRepository, times(1)).findAll();
    }

    @Test
    void getRouteByShipIdAndRouteIdTest() {
        when(routeRepository.findRouteByShipIdAndRouteId(1L, 1L)).thenReturn(mockRouteEntity);
        when(modelMapper.map(mockRouteEntity, RouteDTO.class)).thenReturn(mockRouteDTO);

        RouteDTO result = routeService.getRouteByShipIdAndRouteId(1L, 1L);

        assertNotNull(result);
        assertEquals("NYC -> LA", result.getPorts());
        verify(routeRepository, times(1)).findRouteByShipIdAndRouteId(1L, 1L);
    }

    @Test
    void updateRouteTest() {
        RouteEntity updatedRouteEntity = mockRouteEntity;
        RouteDTO updatedRouteDTO = mockRouteDTO;
        when(routeRepository.findById(1L)).thenReturn(Optional.of(mockRouteEntity));
        updatedRouteEntity.setPorts("Kandla -> KN");
        when(routeRepository.save(updatedRouteEntity)).thenReturn(updatedRouteEntity);
        updatedRouteDTO.setPorts("Kandla -> KN");
        when(modelMapper.map(updatedRouteEntity, RouteDTO.class)).thenReturn(updatedRouteDTO);

        RouteDTO result = routeService.updateRoute(1L, updatedRouteDTO);
        assertNotNull(result);
        assertEquals("Kandla -> KN", result.getPorts());
        verify(routeRepository, times(1)).save(updatedRouteEntity);


    }
    @Test
    void updateRoute_ShouldThrowException_WhenRouteNotFound() {
        Long routeId = 1L;
        when(routeRepository.findById(routeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> routeService.updateRoute(routeId, mockRouteDTO));
    }

    @Test
    void deleteRouteTest() {
        when(routeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(routeRepository).deleteById(1L);
        routeService.deleteRoute(1L);
        verify(routeRepository, times(1)).deleteById(1L);
    }

    @Test
    void  deleteRouteNotFoundTest(){
        when(routeRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> routeService.deleteRoute(1L));
    }
}