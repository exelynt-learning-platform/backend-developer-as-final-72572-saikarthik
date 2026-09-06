package com.example.resource_booking_system.service;

import com.example.resource_booking_system.dto.reservation.ReservationRequest;
import com.example.resource_booking_system.dto.reservation.ReservationResponse;
import com.example.resource_booking_system.entity.Reservation;
import com.example.resource_booking_system.entity.Resource;
import com.example.resource_booking_system.entity.User;
import com.example.resource_booking_system.enums.ReservationStatus;
import com.example.resource_booking_system.exception.ResourceNotFoundException;
import com.example.resource_booking_system.exception.UnauthorizedException;
import com.example.resource_booking_system.repository.ReservationRepository;
import com.example.resource_booking_system.repository.ResourceRepository;
import com.example.resource_booking_system.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                            ResourceRepository resourceRepository,
                            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    // Get all reservations with filtering and pagination
    public Page<ReservationResponse> getReservations(
            Long userId,
            ReservationStatus status,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection) {

        // Validate pagination parameters
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 10;
        if (size > 100) size = 100; // Max 100 items per page

        // Create Sort object
        Sort.Direction direction = Sort.Direction.DESC;
        if ("asc".equalsIgnoreCase(sortDirection)) {
            direction = Sort.Direction.ASC;
        }
        String requestedSort = sortBy != null ? sortBy : "createdAt";
        String safeSort = List.of("createdAt", "updatedAt", "startTime", "endTime", "price", "status")
            .contains(requestedSort) ? requestedSort : "createdAt";
        Sort sort = Sort.by(direction, safeSort);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Check if user is ADMIN or accessing their own reservations
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = currentUser.getRole().toString().equals("ADMIN");

        // ADMIN can filter by any user, USER can only see their own
        Long filterUserId = isAdmin ? userId : currentUser.getId();
        Specification<Reservation> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filterUserId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filterUserId));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page<Reservation> reservations = reservationRepository.findAll(specification, pageable);

        return reservations.map(this::convertToResponse);
    }

    // Get single reservation by ID with ownership check
    public ReservationResponse getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        // Check ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = currentUser.getRole().toString().equals("ADMIN");

        if (!isAdmin && !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only view your own reservations");
        }

        return convertToResponse(reservation);
    }

    // Create new reservation
    public ReservationResponse createReservation(ReservationRequest request) {
        // Get current user from JWT
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate time range
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Get resource
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        if (!resource.getAvailable()) {
            throw new IllegalArgumentException("Resource is not available");
        }

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setResource(resource);
        reservation.setUser(currentUser);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setPrice(request.getPrice() != null ? request.getPrice() : resource.getPrice());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation saved = reservationRepository.save(reservation);
        return convertToResponse(saved);
    }

    // Update reservation
    public ReservationResponse updateReservation(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        // Check ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = currentUser.getRole().toString().equals("ADMIN");

        if (!isAdmin && !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only update your own reservations");
        }

        // Validate time range
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        if (request.getResourceId() != null) {
            Resource resource = resourceRepository.findById(request.getResourceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
            reservation.setResource(resource);
        }

        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        if (request.getPrice() != null) {
            reservation.setPrice(request.getPrice());
        }

        Reservation updated = reservationRepository.save(reservation);
        return convertToResponse(updated);
    }

    // Delete reservation
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        // Check ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = currentUser.getRole().toString().equals("ADMIN");

        if (!isAdmin && !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only delete your own reservations");
        }

        reservationRepository.delete(reservation);
    }

    // Update reservation status (ADMIN only)
    public ReservationResponse updateReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        reservation.setStatus(status);
        Reservation updated = reservationRepository.save(reservation);
        return convertToResponse(updated);
    }

    // Helper method to convert entity to DTO
    private ReservationResponse convertToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setResourceId(reservation.getResource().getId());
        response.setResourceName(reservation.getResource().getName());
        response.setUserId(reservation.getUser().getId());
        response.setUsername(reservation.getUser().getUsername());
        response.setStartTime(reservation.getStartTime());
        response.setEndTime(reservation.getEndTime());
        response.setPrice(reservation.getPrice());
        response.setStatus(reservation.getStatus());
        response.setCreatedAt(reservation.getCreatedAt());
        response.setUpdatedAt(reservation.getUpdatedAt());
        return response;
    }
}
