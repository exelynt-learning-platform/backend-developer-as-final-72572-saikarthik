package com.example.resource_booking_system.controller;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    public ReservationController(ReservationService reservationService){
        this.reservationService=reservationService;
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Reservation> getReservations(){
        return reservationService.getReservations();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Reservation getReservationsById(@PathVariable Long id){
        return reservationService.getReservationsById(id);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Reservation createReservations(@RequestBody Reservation reservation){
        return reservationService.createReservations(reservation);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Reservation updateReservations(@RequestBody Reservation reservation){
        return reservationService.updateReservations(reservation);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String deleteById(@PathVariable Long id){
        return reservationService.deleteById(id);
    }
}
