package com.designhub.Controllers;

import com.designhub.Services.DealService;
import com.designhub.Tables.Deals.DealDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.designhub.Tables.Deals.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/deals")
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    // ========== БАЗОВЫЕ CRUD МЕТОДЫ ==========

    // Получить все сделки
    @GetMapping
    public ResponseEntity<List<DealDto>> getAll() {
        return ResponseEntity.ok(dealService.findAll());
    }

    // Получить сделку по ID
    @GetMapping("/{id}")
    public ResponseEntity<DealDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(dealService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Получить сделки по заказчику
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DealDto>> getByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(dealService.findByCustomerId(customerId));
    }



    // Получить сделки по подрядчику
    @GetMapping("/contractor/{contractorId}")
    public ResponseEntity<List<DealDto>> getByContractorId(@PathVariable Long contractorId) {
        return ResponseEntity.ok(dealService.findByContractorId(contractorId));
    }

    @PutMapping("/{dealId}/select-contractor")
    public ResponseEntity<?> selectContractor(@PathVariable Long dealId, @RequestBody Map<String, Long> body) {
        try {
            Long contractorId = body.get("contractorId");
            dealService.selectContractor(dealId, contractorId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Получить PENDING сделки для подрядчика (входящие заявки)
    @GetMapping("/contractor/{contractorId}/pending")
    public ResponseEntity<List<DealDto>> getPendingForContractor(@PathVariable Long contractorId) {
        return ResponseEntity.ok(dealService.findPendingDealsForContractor(contractorId));
    }

    // Создать новую заявку (оригинал)
    @PostMapping
    public ResponseEntity<?> createDeal(@RequestBody DealDto dto) {
        try {
            DealDto created = dealService.createDeal(dto);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Заявка успешно создана",
                    "dealId", created.id()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ С ОТКЛИКАМИ ==========

    // Заказчик создаёт отклик (копию сделки) для конкретного подрядчика
    @PostMapping("/{dealId}/respond/{contractorId}")
    public ResponseEntity<?> createResponse(@PathVariable Long dealId, @PathVariable Long contractorId) {
        try {
            DealDto response = dealService.createResponse(dealId, contractorId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Отклик отправлен подрядчику",
                    "responseId", response.id()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Подрядчик отправляет сопроводительное письмо
    @PutMapping("/{dealId}/contractor-message")
    public ResponseEntity<?> updateContractorMessage(@PathVariable Long dealId, @RequestBody Map<String, String> body) {
        try {
            String message = body.get("message");
            dealService.updateContractorMessage(dealId, message);
            return ResponseEntity.ok(Map.of("success", true, "message", "Письмо отправлено"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Заказчик подтверждает сделку (выбирает подрядчика)
    @PutMapping("/{dealId}/confirm")
    public ResponseEntity<?> confirmDeal(@PathVariable Long dealId) {
        try {
            dealService.confirmDeal(dealId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Сделка подтверждена, остальные отклики отменены"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ СО СТАТУСАМИ ==========

    // Подрядчик принимает заявку
    @PutMapping("/{dealId}/accept")
    public ResponseEntity<?> acceptDeal(@PathVariable Long dealId) {
        try {
            dealService.acceptDeal(dealId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Заявка принята, сделка активна"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Подрядчик отклоняет заявку
    @PutMapping("/{dealId}/reject")
    public ResponseEntity<?> rejectDeal(@PathVariable Long dealId) {
        try {
            dealService.rejectDeal(dealId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Заявка отклонена"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/{dealId}/approve")
    public ResponseEntity<?> approveDeal(@PathVariable Long dealId) {
        try {
            dealService.approveDeal(dealId);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Завершить сделку
    @PutMapping("/{dealId}/complete")
    public ResponseEntity<?> completeDeal(@PathVariable Long dealId) {
        try {
            dealService.completeDeal(dealId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Сделка завершена"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    // Отменить сделку
    @PutMapping("/{dealId}/cancel")
    public ResponseEntity<?> cancelDeal(@PathVariable Long dealId) {
        try {
            dealService.cancelDeal(dealId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Сделка отменена"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}