package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.*;
import  static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private  RouteController routeController;

    private RouteDTO routeDTO;
    private ShipDTO shipDTO;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc =  MockMvcBuilders.standaloneSetup(routeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        shipDTO =new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
        routeDTO= new RouteDTO(10L, shipDTO, "NYC -> LA" ,500.5 ,72.5, LocalDateTime.now(),LocalDateTime.now());
    }

    @Test
    void assignRouteToShipTest() throws Exception {
        when(routeService.assignRouteToShip(1L, routeDTO)).thenReturn(routeDTO);
        mockMvc.perform(post("/ships/1/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Route successfully assigned to Ship ID: 1"))
                .andExpect(jsonPath("$.data.ports").value("NYC -> LA"))
                .andExpect(jsonPath("$.data.ship.name").value("Titanic"));
        verify(routeService, times(1)).assignRouteToShip(1L,routeDTO);
    }

    @Test
    void getRoutesByShipIdTest() throws Exception {
        when(routeService.getRoutesByShip(1L)).thenReturn(List.of(routeDTO));
        mockMvc.perform(get("/ships/1/route")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Route retrieved successfully! Ship ID: 1"))
                .andExpect(jsonPath("$.data.[0].ports").value("NYC -> LA"))
                .andExpect(jsonPath("$.data.[0].ship.name").value("Titanic"));
        verify(routeService, times(1)).getRoutesByShip(1L);
    }

    @Test
    void getRouteByShipIdAndRouteIdTest() throws Exception {
        when(routeService.getRouteByShipIdAndRouteId(1L,10L)).thenReturn(routeDTO);
        mockMvc.perform(get("/ships/1/route/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Route successfully assigned to Ship ID: 1"))
                .andExpect(jsonPath("$.data.ports").value("NYC -> LA"))
                .andExpect(jsonPath("$.data.ship.name").value("Titanic"));
        verify(routeService, times(1)).getRouteByShipIdAndRouteId(1L, 10L);
    }

    @Test
    void updateRouteByIdTest() throws Exception {
        routeDTO.setPorts("Kandla");
        when(routeService.updateRoute(10L,routeDTO)).thenReturn(routeDTO);
        mockMvc.perform(put("/ships/1/route/10", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Updated Route successfully assigned to Ship ID: "))
                        .andExpect(jsonPath("$.data.ports").value("Kandla"))
                        .andExpect(jsonPath("$.data.ship.name").value("Titanic"));

        verify(routeService, times(1)).updateRoute(10L, routeDTO);

    }

    @Test
    void  deleteRouteTest_Success() throws Exception{
        doNothing().when(routeService).deleteRoute(1L);
        mockMvc.perform(delete("/ships/1/route/1", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // ✅ 200 OK
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Route removed successfully"));

        verify(routeService, times(1)).deleteRoute(1L);
    }

    @Test
    void deleteRouteTest_Fail() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(routeService).deleteRoute(11L);

        mockMvc.perform(delete("/ships/1/route/11", 11L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // ✅ 404 NOT FOUND
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Route Not Found"));

        verify(routeService, times(1)).deleteRoute(11L);
    }
}