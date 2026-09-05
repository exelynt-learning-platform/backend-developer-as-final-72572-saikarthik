package com.example.resource_booking_system.service;

import com.example.resource_booking_system.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.resource_booking_system.repository.ResourceRepository;
import com.example.resource_booking_system.entity.Resource;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resource> getResources(){
        return resourceRepository.findAll();
    }
    public Resource getResourcesById(@PathVariable Long id){
        return resourceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
    }
    public Resource createResources( Resource resource){
        return resourceRepository.save(resource);
    }
    public Resource createResourcesById(Long id,Resource resource){
        Resource res=resourceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
        res.setName(resource.getName());
        res.setDescription(resource.getDescription());
        res.setType(resource.getType());
        res.setPrice(resource.getPrice());
        res.setAvailable(resource.getAvailable());
        return resourceRepository.save(res);
    }
    public Resource updateResource(Long id,Resource resource){
        Resource res=resourceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
        res.setName(resource.getName());
        res.setDescription(resource.getDescription());
        res.setType(resource.getType());
        res.setPrice(resource.getPrice());
        res.setAvailable(resource.getAvailable());
        res.setCreatedAt(resource.getCreatedAt());
        return resourceRepository.save(res);
    }
    public String deleteResource(Long id){
        Resource res=resourceRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("id not found"));
        resourceRepository.delete(res);
        return "Deleted successfully";
    }
}
