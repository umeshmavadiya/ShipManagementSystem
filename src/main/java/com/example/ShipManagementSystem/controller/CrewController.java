package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.CrewDTO;
import com.example.ShipManagementSystem.service.CrewService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships/{shipId}/crew")
public class CrewController {

    private final CrewService crewService;

    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CrewDTO>> assignCrewToShip(@PathVariable Long shipId, @RequestBody CrewDTO crew){
        CrewDTO crewData = crewService.assignCrewToShip(shipId,crew);
        return new ResponseEntity<>(new ApiResponse<>(200, "Crew successfully assigned to Ship ID: "+shipId, crewData), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CrewDTO>>> getCrewByShip(@PathVariable Long shipId){
        List<CrewDTO> crewData = crewService.getCrewByShip(shipId);
        return new ResponseEntity<>(new ApiResponse<>(200, "Ship retrieved successfully! Ship ID: "+shipId, crewData), HttpStatus.OK);
    }
    @GetMapping("/{crewId}")
    public ResponseEntity<ApiResponse<CrewDTO>> getCrewByShipIdAndCrewId(@PathVariable Long shipId, @PathVariable Long crewId){
        try {
            CrewDTO crewData = crewService.getCrewByShipIdAndCrewId(shipId, crewId);
            return new ResponseEntity<>(new ApiResponse<>(200, "Crew retrieved successfully! Ship ID: " + shipId, crewData), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(new ApiResponse<>(404, "Crew not found", null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>(500, "Error GetCrewByShipIdAndCrewId Crew", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{crewId}")
    public ResponseEntity<ApiResponse<CrewDTO>>  updateCrewPosition(@PathVariable Long crewId, @RequestBody CrewDTO crew) {
        CrewDTO crewData = crewService.updateCrewPosition(crewId,crew.getPosition());
        return new ResponseEntity<>(new ApiResponse<>(200, "Crew Position successfully  updated ", crewData), HttpStatus.OK);
    }

    @DeleteMapping("/{crewId}")
    public ResponseEntity<ApiResponse<String>> removeCrewFromShip(@PathVariable Long crewId) {
        try {
            crewService.removeCrewFromShip(crewId);
            return new ResponseEntity<>(new ApiResponse<>(200, "Crew removed successfully", null), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(new ApiResponse<>(404, "Crew not found to removed", null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>(500, "Error removed Crew", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
