package com.example.resource_booking_system.service;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.exception.ResourceNotFoundException;
import com.example.resource_booking_system.repository.ReservationRepository;
import com.example.resource_booking_system.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    public ReservationService(ReservationRepository reservationRepository){
        this.reservationRepository=reservationRepository;
    }
    public List<Reservation> getReservations(){
        return reservationRepository.findAll();
    }
    public Reservation getReservationsById(Long id){
        return reservationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
    }
    public Reservation createReservations(Reservation reservation){
        return reservationRepository.save(reservation);
    }
    public Reservation updateReservations(Reservation reservation){
        Reservation res=reservationRepository.findById(reservation.getId()).orElseThrow(()-> new ResourceNotFoundException("id not found"));
        res.setResource(reservation.getResource());
        res.setUser(reservation.getUser());
        res.setStart_time(reservation.getStart_time());
        res.setEnd_time(reservation.getEnd_time());
        res.setPrice(reservation.getPrice());
        res.setStatus(reservation.getStatus());
        res.setCreatedAt(reservation.getCreatedAt());
        return reservationRepository.save(res);

    }
    public String deleteById(Long id){
        Reservation res=reservationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
        reservationRepository.delete(res);
        return "Successfully Deleted";
    }
}
