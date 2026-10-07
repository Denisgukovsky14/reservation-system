package com.sarcofuckusLesson.designhub.Controllers;

import com.sarcofuckusLesson.designhub.AuthenticatedUser;
import com.sarcofuckusLesson.designhub.Services.ContractorService;
import com.sarcofuckusLesson.designhub.Services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import com.sarcofuckusLesson.designhub.Tables.Contractors.*;
import com.sarcofuckusLesson.designhub.Tables.Customers.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ContractorService contractorService;
    private final CustomerService customerService;

    public ProfileController(ContractorService contractorService, CustomerService customerService) {
        this.contractorService = contractorService;
        this.customerService = customerService;
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestParam String type,
            @RequestBody Map<String, Object> data,
            @AuthenticationPrincipal AuthenticatedUser currentUser) {
        try {
            Object idRaw = data.get("id");
            if (idRaw == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Не указан id"));
            }
            Long targetId = ((Number) idRaw).longValue();

            if (!currentUser.id().equals(targetId) || !currentUser.userType().equals(type)) {
                return ResponseEntity.status(403).body(Map.of("message", "Нельзя изменить чужой профиль"));
            }

            if ("contractor".equals(type)) {
                contractorService.updateProfile(data);
            } else if ("customer".equals(type)) {
                customerService.updateProfile(data);
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Неверный тип пользователя"));
            }
            return ResponseEntity.ok(Map.of("success", true, "message", "Профиль обновлён"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Ошибка при обновлении профиля: " + e.getMessage()));
        }
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getProfile(@PathVariable String type, @PathVariable Long id) {
        try {
            if ("contractor".equals(type)) {
                ContractorDto contractor = contractorService.findById(id);
                Map<String, Object> response = new HashMap<>();
                response.put("type", "contractor");
                response.put("id", contractor.id());
                response.put("firstName", contractor.firstName());
                response.put("lastName", contractor.lastName());
                response.put("patronymic", contractor.patronymic());
                response.put("companyName", contractor.companyName());
                response.put("businessType", contractor.businessType());
                response.put("address", contractor.address());
                response.put("phone", contractor.phone());
                response.put("email", contractor.email());
                response.put("profileImage", contractor.profileImage());
                response.put("aboutCompany", contractor.aboutCompany());
                response.put("servicesIds", contractor.servicesIds());
                response.put("createdAt", contractor.createdAt());
                return ResponseEntity.ok(response);

            } else if ("customer".equals(type)) {
                CustomerDto customer = customerService.findById(id);
                Map<String, Object> response = new HashMap<>();
                response.put("type", "customer");
                response.put("id", customer.id());
                response.put("firstName", customer.name());      // name → firstName для единообразия
                response.put("lastName", customer.surname());   // surname → lastName
                response.put("patronymic", customer.patronymic());
                response.put("companyName", customer.companyName());
                response.put("businessType", customer.businessType());
                response.put("address", customer.address());
                response.put("phone", customer.phone());
                response.put("email", customer.email());
                response.put("profileImage", customer.profileImage());
                response.put("about", customer.about());        // about → описание
                response.put("createdAt", customer.createdAt());
                return ResponseEntity.ok(response);

            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Неверный тип пользователя"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}