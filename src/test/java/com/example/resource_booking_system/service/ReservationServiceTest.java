package com.example.resource_booking_system.service;

import com.example.resource_booking_system.dto.reservation.ReservationRequest;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.repository.ReservationRepository;
import com.example.resource_booking_system.repository.ResourceRepository;
import com.example.resource_booking_system.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ResourceRepository resourceRepository = mock(ResourceRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ReservationService reservationService = new ReservationService(
            reservationRepository, resourceRepository, userRepository);

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createReservationRejectsEndTimeBeforeStartTime() {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("user", null));

        ReservationRequest request = new ReservationRequest(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusHours(1),
                null);

        assertThrows(IllegalArgumentException.class,
                () -> reservationService.createReservation(request));
    }
}
