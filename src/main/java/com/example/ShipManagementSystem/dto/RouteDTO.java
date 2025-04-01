package com.example.ShipManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    private Long routeId;
    private ShipDTO ship;
    private String ports;
    private double distance;
    private double estimatedTime;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
