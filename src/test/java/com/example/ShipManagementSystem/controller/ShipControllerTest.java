package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.service.ShipService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShipControllerTest {

    @Mock
    private ShipService shipService;

    @InjectMocks
    private ShipController shipController;

    private ShipDTO mockShipDTO;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc =  MockMvcBuilders.standaloneSetup(shipController).build();
        mockShipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
    }

    @Test
    void getAllShipsDetailsTest() throws Exception {
        List<ShipDTO> shipList = Arrays.asList(mockShipDTO);
        when(shipService.getAllShipDetails()).thenReturn(shipList);
        mockMvc.perform(get("/ships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].name").value("Titanic"))
                .andExpect(jsonPath("$.data.[0].type").value("Cruise"));

        verify(shipService, times(1)).getAllShipDetails();
    }

    @Test
    void createNewShipTest() throws Exception {
        when(shipService.createNewShip(mockShipDTO)).thenReturn(mockShipDTO);

        mockMvc.perform(post("/ships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockShipDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Titanic"))
                .andExpect(jsonPath("$.data.type").value("Cruise"));
        verify(shipService, times(1)).createNewShip(mockShipDTO);
    }

    @Test
    void getShipDetailsByIdTest() throws Exception {
        when(shipService.getShipDetailsById(1L)).thenReturn(mockShipDTO);

        mockMvc.perform(get("/ships/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Titanic"))
                .andExpect(jsonPath("$.data.type").value("Cruise"));
        verify(shipService, times(1)).getShipDetailsById(1L);
    }

    @Test
    void updateShipDetailByIdTest() throws Exception {
        mockShipDTO.setName("Navig8");
        mockShipDTO.setType("Passanger");
        when(shipService.updateShipDetailsById(eq(1L), any(ShipDTO.class))).thenReturn(mockShipDTO);

        mockMvc.perform(put("/ships/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(mockShipDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Navig8"))
                .andExpect(jsonPath("$.data.type").value("Passanger"));
        verify(shipService, times(1)).updateShipDetailsById(eq(1L), any(ShipDTO.class));
    }

    @Test
    void deleteShipDetailByIdTest_Success() throws Exception {
        doNothing().when(shipService).deleteShipDetailById(1L);

        mockMvc.perform(delete("/ships/1", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // ✅ 200 OK
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Ship deleted successfully"));
        verify(shipService, times(1)).deleteShipDetailById(1L);
    }

    @Test
    void deleteShipDetailByIdTest_Fail() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(shipService).deleteShipDetailById(11L);
        mockMvc.perform(delete("/ships/11", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Ship not found to delete"));
        verify(shipService, times(1)).deleteShipDetailById(11L);
    }

    void testPrintMockCrewDTOAsJson(ApiResponse<List<ShipDTO>> mockData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mockData);

        System.out.println("Mock Crew DTO (JSON): \n" + json);
    }
}