package com.example.resource_booking_system.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long resourceId;
    private LocalDateTime sartTime;
    private LocalDateTime endTime;
}
