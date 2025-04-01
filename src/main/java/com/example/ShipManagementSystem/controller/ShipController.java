package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.ShipDTO;
import com.example.ShipManagementSystem.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "ships")
public class ShipController {

    @Autowired
    private ShipService shipService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShipDTO>>> getAllShipsDetails(){
      List<ShipDTO> ships= shipService.getAllShipDetails();
      return new ResponseEntity<>(new ApiResponse<>(200, "Ship retrieved successfully", ships), HttpStatus.OK);
    }

    @PostMapping
        public  ResponseEntity<ApiResponse<ShipDTO>>  createNewShip(@RequestBody ShipDTO shipDTO){
    ShipDTO shipData =  shipService.createNewShip(shipDTO);
    return new ResponseEntity<>(new ApiResponse<>(200, "Ship Created successfully", shipData), HttpStatus.OK);
    }

    @GetMapping("/{shipId}")
    public ResponseEntity<ApiResponse<ShipDTO>> getShipDetailsById(@PathVariable Long shipId){
        ShipDTO shipData =  shipService.getShipDetailsById(shipId);
        return new ResponseEntity<>(new ApiResponse<>(200, "Ship retrieved successfully", shipData), HttpStatus.OK);

    }

    @PutMapping("/{shipId}")
    public  ResponseEntity<ApiResponse<ShipDTO>> updateShipDetailById(@PathVariable Long shipId, @RequestBody ShipDTO shipDTO){
        ShipDTO shipData =  shipService.updateShipDetailsById(shipId,shipDTO);
        return new ResponseEntity<>(new ApiResponse<>(200, "Ship Detail update successfully", shipData), HttpStatus.OK);
    }

    @DeleteMapping("/{shipId}")
    public ResponseEntity<ApiResponse<String>> deleteShipDetailById(@PathVariable Long shipId){
        try {
            shipService.deleteShipDetailById(shipId);
            return new ResponseEntity<>(new ApiResponse<>(200, "Ship deleted successfully", null), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(new ApiResponse<>(404, "Ship not found to delete", null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>(500, "Error deleting Ship", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
