package com.example.resource_booking_system.repository;

import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    // Find all reservations by user ID with pagination
    Page<Reservation> findByUserId(Long userId, Pageable pageable);
    
    // Find reservations by status
    Page<Reservation> findByStatus(ReservationStatus status, Pageable pageable);
    
    // Find reservations by user ID and status
    Page<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status, Pageable pageable);
    
    // Find reservations by price range
    @Query("SELECT r FROM Reservation r WHERE r.price >= :minPrice AND r.price <= :maxPrice")
    Page<Reservation> findByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                       @Param("maxPrice") BigDecimal maxPrice, 
                                       Pageable pageable);
    
    // Find user's reservations by price range
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.price >= :minPrice AND r.price <= :maxPrice")
    Page<Reservation> findByUserIdAndPriceRange(@Param("userId") Long userId,
                                                @Param("minPrice") BigDecimal minPrice,
                                                @Param("maxPrice") BigDecimal maxPrice,
                                                Pageable pageable);
    
    // Find user's reservations by status and price range
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.status = :status AND r.price >= :minPrice AND r.price <= :maxPrice")
    Page<Reservation> findByUserIdStatusAndPriceRange(@Param("userId") Long userId,
                                                      @Param("status") ReservationStatus status,
                                                      @Param("minPrice") BigDecimal minPrice,
                                                      @Param("maxPrice") BigDecimal maxPrice,
                                                      Pageable pageable);
    
    // Find reservations by user and status
    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);
}
