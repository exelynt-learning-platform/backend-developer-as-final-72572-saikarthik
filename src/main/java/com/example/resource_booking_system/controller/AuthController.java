package com.example.resource_booking_system.controller;
import com.example.resource_booking_system.dto.login.LoginRequest;
import com.example.resource_booking_system.dto.login.LoginResponse;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return authService.register(user);
    }
    @PostMapping("/login")
    public String  login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
