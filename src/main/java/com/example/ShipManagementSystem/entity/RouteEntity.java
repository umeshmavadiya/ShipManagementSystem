package com.example.ShipManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "routes")
@AllArgsConstructor
@NoArgsConstructor
public class RouteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long routeId;

    @ManyToOne
    @JoinColumn(name = "ship_id", nullable = false)
    private ShipEntity ship;

    @Column(nullable = false)
    private String ports;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private double estimatedTime;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;
}
