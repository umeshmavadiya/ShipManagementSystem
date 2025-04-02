package com.example.ShipManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewDTO {
    private Long crewId;
    private String name;
    private String position;
    private ShipDTO ship;
}
