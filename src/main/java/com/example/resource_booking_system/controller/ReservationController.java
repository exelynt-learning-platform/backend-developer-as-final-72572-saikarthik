package com.example.resource_booking_system.controller;

import com.example.resource_booking_system.dto.reservation.ReservationRequest;
import com.example.resource_booking_system.dto.reservation.ReservationResponse;
import com.example.resource_booking_system.enums.ReservationStatus;
import com.example.resource_booking_system.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Get all reservations with filtering and pagination
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<ReservationResponse>> getReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<ReservationResponse> reservations = reservationService.getReservations(
                userId, status, minPrice, maxPrice, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(reservations);
    }

    // Get single reservation by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        ReservationResponse reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    // Create new reservation
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody ReservationRequest request) {
        ReservationResponse reservation = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    // Update reservation
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {
        ReservationResponse reservation = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(reservation);
    }

    // Delete reservation
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully");
    }

    // Update reservation status (ADMIN only)
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponse> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {
        ReservationResponse reservation = reservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(reservation);
    }
}
