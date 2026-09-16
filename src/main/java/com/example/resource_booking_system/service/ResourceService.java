package com.example.resource_booking_system.service;

import com.example.resource_booking_system.dto.resources.ResourceRequest;
import com.example.resource_booking_system.dto.resources.ResourceResponse;
import com.example.resource_booking_system.entity.Resource;
import com.example.resource_booking_system.exception.ResourceNotFoundException;
import com.example.resource_booking_system.repository.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ResourceService {
    private final ResourceRepository resourceRepository;

    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    // Get all resources with pagination
    public Page<ResourceResponse> getResources(Integer page, Integer size, String sortBy) {
        if (page == null || page < 0) page = 0;
        if (size == null || size <= 0) size = 10;
        if (size > 100) size = 100;

        String requestedSort = sortBy != null ? sortBy : "createdAt";
        String safeSort = Set.of("createdAt", "updatedAt", "name", "type", "price", "available")
            .contains(requestedSort) ? requestedSort : "createdAt";
        Sort sort = Sort.by(Sort.Direction.DESC, safeSort);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Resource> resources = resourceRepository.findAll(pageable);
        return resources.map(this::convertToResponse);
    }

    // Get single resource by ID
    public ResourceResponse getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return convertToResponse(resource);
    }

    // Create resource (ADMIN only)
    public ResourceResponse createResource(ResourceRequest request) {
        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setType(request.getType());
        resource.setPrice(request.getPrice());
        resource.setAvailable(request.getAvailable());

        Resource saved = resourceRepository.save(resource);
        return convertToResponse(saved);
    }

    // Update resource (ADMIN only)
    public ResourceResponse updateResource(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setType(request.getType());
        resource.setPrice(request.getPrice());
        resource.setAvailable(request.getAvailable());

        Resource updated = resourceRepository.save(resource);
        return convertToResponse(updated);
    }

    // Delete resource (ADMIN only)
    public void deleteResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        resourceRepository.delete(resource);
    }

    // Helper method
    private ResourceResponse convertToResponse(Resource resource) {
        ResourceResponse response = new ResourceResponse();
        response.setId(resource.getId());
        response.setName(resource.getName());
        response.setDescription(resource.getDescription());
        response.setType(resource.getType());
        response.setPrice(resource.getPrice());
        response.setAvailable(resource.getAvailable());
        response.setCreatedAt(resource.getCreatedAt());
        response.setUpdatedAt(resource.getUpdatedAt());
        return response;
    }
}
