package com.example.resource_booking_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.resource_booking_system.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
