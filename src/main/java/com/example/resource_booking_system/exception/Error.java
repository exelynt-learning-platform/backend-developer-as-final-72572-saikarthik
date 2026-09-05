package com.example.resource_booking_system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    private Date timestamp;
    private String message;
    private String Details;
}
