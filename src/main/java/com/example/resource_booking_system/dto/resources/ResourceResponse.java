package com.example.resource_booking_system.dto.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private Long id;
    private String name;
    private String description;
    private String type;
    private BigDecimal price;
    private Boolean available;
}
