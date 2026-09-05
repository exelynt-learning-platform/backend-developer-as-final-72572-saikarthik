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
    private Long userId;
    private Long resourceId;
    private Long resourceName;
    private BigDecimal price;
    private ReservationStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
