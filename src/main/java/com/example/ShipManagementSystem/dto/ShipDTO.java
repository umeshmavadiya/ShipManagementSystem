package com.example.ShipManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipDTO {
    private Long shipId;
    private String name;
    private String type;
    private Number capacity;
    private String status;
}
