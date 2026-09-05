package com.example.resource_booking_system.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private String token;
    public LoginResponse(String token){
        this.token=token;
    }
}
