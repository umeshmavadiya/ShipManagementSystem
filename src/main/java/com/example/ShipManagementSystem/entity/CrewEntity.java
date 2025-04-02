package com.example.ShipManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "crew")
@AllArgsConstructor
@NoArgsConstructor

public class CrewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long crewId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String position;

    @ManyToOne
    @JoinColumn(name = "ship_id")
    private ShipEntity ship;
}
