package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.exception.ResourceNotFoundException;
import com.example.ShipManagementSystem.service.CrewService;
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
import static org.mockito.Mockito.*;
import  static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import java.util.List;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class  CrewControllerTest{


    @InjectMocks
    private CrewController crewController;

    @Mock
    private CrewService crewService;

    private MockMvc mockMvc;

    private ShipDTO shipDTO;

    private  CrewDTO crewDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(crewController).build();
//        MockitoAnnotations.openMocks(this);
//
        shipDTO = new ShipDTO(1L, "Titanic", "Cruise", 5000, "Active");
        crewDTO = new CrewDTO(1L, "Umesh Mavadiya","Master", shipDTO);
    }
    @Test
    void getCrewByShipTest() throws Exception {
        when(crewService.getCrewByShip(1L)).thenReturn(List.of(crewDTO));
        mockMvc.perform(get("/ships/1/crew"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.[0].name").value("Umesh Mavadiya"))
                .andExpect(jsonPath("$.data.[0].ship.name").value("Titanic"));

        verify(crewService, times(1)).getCrewByShip(1L);
    }

    @Test
    void  assignCrewToShipTest() throws Exception{
        when(crewService.assignCrewToShip(1L,crewDTO)).thenReturn(crewDTO);
        mockMvc.perform(post("/ships/1/crew")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(crewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Crew successfully assigned to Ship ID: 1"))
                .andExpect(jsonPath("$.data.name").value("Umesh Mavadiya"))
                .andExpect(jsonPath("$.data.ship.name").value("Titanic"));
        verify(crewService, times(1)).assignCrewToShip(1L,crewDTO);
    }

    @Test
    void getCrewByShipIdAndCrewIdTest_Sucess() throws Exception{
        when(crewService.getCrewByShipIdAndCrewId(1L,1L)).thenReturn(crewDTO);
        mockMvc.perform(get("/ships/1/crew/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Crew retrieved successfully! Ship ID: 1"))
                .andExpect(jsonPath("$.data.name").value("Umesh Mavadiya"))
                .andExpect(jsonPath("$.data.ship.name").value("Titanic"));
        verify(crewService, times(1)).getCrewByShipIdAndCrewId(1L,1L);
    }

    @Test
    void getCrewByShipIdAndCrewIdTest_Fail() throws Exception{
        when(crewService.getCrewByShipIdAndCrewId(1L,165L)).thenThrow(new ResourceNotFoundException("Ship ID: 1 with Crew not found ID:165"));
        mockMvc.perform(get("/ships/1/crew/165")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Ship ID: 1 with Crew not found ID:165"));
        verify(crewService, times(1)).getCrewByShipIdAndCrewId(1L,165L);
    }

    @Test
    void updateCrewPositionTest() throws Exception {
        crewDTO.setPosition("Main Master");
        when(crewService.updateCrewPosition(1L,"Main Master")).thenReturn(crewDTO);
        mockMvc.perform(put("/ships/1/crew/1")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(crewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Crew Position successfully  updated "))
                .andExpect(jsonPath("$.data.position").value("Main Master"));
        verify(crewService, times(1)).updateCrewPosition(1L,"Main Master");
    }

    @Test
    void  removeCrewFromShipTest_Success() throws Exception{
        doNothing().when(crewService).removeCrewFromShip(1L);
        mockMvc.perform(delete("/ships/1/crew/1", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // ✅ 200 OK
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Crew removed successfully"));

        verify(crewService, times(1)).removeCrewFromShip(1L);
    }

    @Test
    void  removeCrewFromShipTest_Fail() throws Exception{
        doThrow(new EmptyResultDataAccessException(1)).when(crewService).removeCrewFromShip(11L);

        mockMvc.perform(delete("/ships/1/crew/11", 11L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // ✅ 404 NOT FOUND
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.message").value("Crew not found to removed"));

        verify(crewService, times(1)).removeCrewFromShip(11L);
    }





    void testPrintMockCrewDTOAsJson(ApiResponse<List<CrewDTO>> mockData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mockData);

        System.out.println("Mock Crew DTO (JSON): \n" + json);
    }
}