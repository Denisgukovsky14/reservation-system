package com.designhub.Controllers;

import com.designhub.Services.ContractorService;
import com.designhub.Tables.Contractors.ContractorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.designhub.Tables.Contractors.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contractors")
public class ContractorController {

    private final ContractorService contractorService;

    public ContractorController(ContractorService contractorService) {
        this.contractorService = contractorService;
    }

    // ========== БАЗОВЫЕ МЕТОДЫ ==========

    // Получить всех подрядчиков
    @GetMapping
    public ResponseEntity<List<ContractorDto>> getAll() {
        return ResponseEntity.ok(contractorService.findAll());
    }

    // Получить подрядчика по ID
    @GetMapping("/{id}")
    public ResponseEntity<ContractorDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contractorService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ========== ПОИСК ПО УСЛУГАМ ==========

    // Поиск подрядчиков по ID услуг (пересечение)
    @GetMapping("/search-by-services")
    public ResponseEntity<List<ContractorDto>> searchByServices(
            @RequestParam String servicesIds,
            @RequestParam(required = false) Long excludeContractorId) {

        if (servicesIds == null || servicesIds.trim().isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<Long> requiredServiceIds = Arrays.stream(servicesIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());

        List<ContractorDto> allContractors = contractorService.findAll();

        List<ContractorDto> result = allContractors.stream()
                .filter(contractor -> {
                    // Исключаем только если excludeContractorId передан и совпадает
                    if (excludeContractorId != null && contractor.id().equals(excludeContractorId)) {
                        return false;
                    }
                    if (contractor.servicesIds() == null || contractor.servicesIds().trim().isEmpty()) {
                        return false;
                    }
                    List<Long> contractorServiceIds = Arrays.stream(contractor.servicesIds().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(Long::parseLong)
                            .collect(Collectors.toList());

                    return requiredServiceIds.stream().anyMatch(contractorServiceIds::contains);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // Поиск подрядчиков по конкретной услуге (по ID услуги)
    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<ContractorDto>> findByService(@PathVariable Long serviceId) {
        List<ContractorDto> allContractors = contractorService.findAll();

        List<ContractorDto> result = allContractors.stream()
                .filter(contractor -> {
                    if (contractor.servicesIds() == null) return false;
                    List<Long> contractorServiceIds = Arrays.stream(contractor.servicesIds().split(","))
                            .map(String::trim)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());
                    return contractorServiceIds.contains(serviceId);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ========== ПОИСК ПО РЕГИОНУ ==========

    // Поиск подрядчиков по региону
    @GetMapping("/by-region/{region}")
    public ResponseEntity<List<ContractorDto>> findByRegion(@PathVariable String region) {
        List<ContractorDto> allContractors = contractorService.findAll();

        List<ContractorDto> result = allContractors.stream()
                .filter(contractor -> contractor.address() != null && contractor.address().toLowerCase().contains(region.toLowerCase()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // ========== ПОИСК ПО МАТЕРИАЛАМ ==========

    // Поиск подрядчиков по материалу (проверяет вхождение в services_ids через описание услуги)
    // Упрощённая версия — ищем по полю about_company
    @GetMapping("/by-material/{material}")
    public ResponseEntity<List<ContractorDto>> findByMaterial(@PathVariable String material) {
        List<ContractorDto> allContractors = contractorService.findAll();

        List<ContractorDto> result = allContractors.stream()
                .filter(contractor -> contractor.aboutCompany() != null &&
                        contractor.aboutCompany().toLowerCase().contains(material.toLowerCase()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
