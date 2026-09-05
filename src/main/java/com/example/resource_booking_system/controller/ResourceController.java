package com.example.resource_booking_system.controller;

import com.example.resource_booking_system.entity.Resource;
import com.example.resource_booking_system.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Resource> getResources(){
        return resourceService.getResources();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Resource getResourcesById(@PathVariable Long id){
        return resourceService.getResourcesById(id);
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Resource createResources(@RequestBody Resource resource){
        return resourceService.createResources(resource);
    }
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Resource createResourcesById(@PathVariable Long id,@RequestBody Resource resource){
        return resourceService.createResourcesById(id,resource);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Resource updateResource(@PathVariable Long id,@RequestBody Resource resource){
        return resourceService.updateResource(id,resource);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteResource(@PathVariable Long id){
        return resourceService.deleteResource(id);
    }
}