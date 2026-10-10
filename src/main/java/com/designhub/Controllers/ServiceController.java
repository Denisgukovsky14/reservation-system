package com.designhub.Controllers;

import com.designhub.Tables.Services.ServiceDto;
import com.designhub.Services.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // Получить все услуги
    @GetMapping("/all")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll());
    }

    // Получить услуги по ID (через запятую)
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getServicesByIds(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .toList();

        return ResponseEntity.ok(serviceService.findByIds(idList));
    }
}