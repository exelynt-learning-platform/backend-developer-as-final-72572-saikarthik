package com.example.resource_booking_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.resource_booking_system.entity.Resource;


public interface ResourceRepository extends JpaRepository<Resource, Long> {

}
