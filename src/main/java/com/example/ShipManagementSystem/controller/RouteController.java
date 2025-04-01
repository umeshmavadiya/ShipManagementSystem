package com.example.ShipManagementSystem.controller;

import com.example.ShipManagementSystem.dto.ApiResponse;
import com.example.ShipManagementSystem.dto.RouteDTO;
import com.example.ShipManagementSystem.service.RouteService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ships/{shipId}/route")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RouteDTO>> assignRouteToShip(@PathVariable Long shipId, @RequestBody RouteDTO route){
        RouteDTO routeData = routeService.assignRouteToShip(shipId,route);
        return new ResponseEntity<>(new ApiResponse<>(200, "Route successfully assigned to Ship ID: "+shipId, routeData), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteDTO>>> getRoutesByShipId(@PathVariable Long shipId){
        List<RouteDTO> roteList = routeService.getRoutesByShip(shipId);
        return new ResponseEntity<>(new ApiResponse<>(200, "Route retrieved successfully! Ship ID: "+shipId, roteList), HttpStatus.OK);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<ApiResponse<RouteDTO>> getRouteByShipIdAndRouteId(@PathVariable Long shipId,@PathVariable Long routeId){
        RouteDTO routeData = routeService.getRouteByShipIdAndRouteId(shipId,routeId);
        return new ResponseEntity<>(new ApiResponse<>(200, "Route successfully assigned to Ship ID: "+shipId, routeData), HttpStatus.OK);
    }

    @PutMapping("/{routeId}")
    public ResponseEntity<ApiResponse<RouteDTO>> updateRouteById(@PathVariable Long routeId, @RequestBody RouteDTO route){
        RouteDTO  routeData = routeService.updateRoute(routeId,route);
        return new ResponseEntity<>(new ApiResponse<>(200, "Updated Route successfully assigned to Ship ID: ", routeData), HttpStatus.OK);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<ApiResponse<String>> deleteRoute(@PathVariable Long routeId){
        try {
            routeService.deleteRoute(routeId);
            return new ResponseEntity<>(new ApiResponse<>(200, "Route removed successfully", null), HttpStatus.OK);
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>(new ApiResponse<>(404, "Route not found to removed", null), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(new ApiResponse<>(500, "Error removed Route", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
