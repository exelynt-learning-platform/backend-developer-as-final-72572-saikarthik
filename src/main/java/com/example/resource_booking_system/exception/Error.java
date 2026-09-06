package com.example.resource_booking_system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private LocalDateTime timestamp;
    private String message;
    private String Details;
}
