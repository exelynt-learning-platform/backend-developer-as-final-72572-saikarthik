package com.example.resource_booking_system.dto.reservation;

import com.example.resource_booking_system.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private Long userId;
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
