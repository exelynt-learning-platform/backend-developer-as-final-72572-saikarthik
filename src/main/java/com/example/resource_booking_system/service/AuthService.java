package com.example.resource_booking_system.service;

import com.example.resource_booking_system.dto.login.LoginRequest;
import com.example.resource_booking_system.dto.login.LoginResponse;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.repository.UserRepository;
import com.example.resource_booking_system.security.CustomUserDetailsService;
import com.example.resource_booking_system.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    public AuthService(JwtTokenProvider jwtService, @Lazy AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }


    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtService;
    
    public User register(User user){
        user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername( String username) throws UsernameNotFoundException {
        Optional<User> byUsername=userRepository.findByUsername(username);
        if(byUsername.isEmpty()) {throw new UsernameNotFoundException("user not found" );}
        return new CustomUserDetailsService(byUsername.get());
    }
    public String login(LoginRequest loginRequest){
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles= authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return jwtService.generateToken(loginRequest.getUsername(), roles);
    }
}
