package com.sarcofuckusLesson.designhub.Services;

import com.sarcofuckusLesson.designhub.Tables.Deals.DealDto;
import com.sarcofuckusLesson.designhub.Tables.Deals.DealEntity;
import com.sarcofuckusLesson.designhub.Tables.Deals.DealRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import com.sarcofuckusLesson.designhub.dealStatus;

@Service
public class DealService {

    private final DealRepository dealRepository;

    public DealService(DealRepository dealRepository) {
        this.dealRepository = dealRepository;
    }

    // ========== БАЗОВЫЕ МЕТОДЫ ==========

    public List<DealDto> findAll() {
        return dealRepository.findAll()
                .stream()
                .map(DealDto::new)
                .collect(Collectors.toList());
    }

    public DealDto findById(Long id) {
        DealEntity entity = dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена с id: " + id));
        return new DealDto(entity);
    }

    public List<DealDto> findByCustomerId(Long customerId) {
        return dealRepository.findByCustomerId(customerId)
                .stream()
                .map(DealDto::new)
                .collect(Collectors.toList());
    }

    public List<DealDto> findByContractorId(Long contractorId) {
        return dealRepository.findByContractorId(contractorId)
                .stream()
                .map(DealDto::new)
                .collect(Collectors.toList());
    }

    public List<DealDto> findPendingDealsForContractor(Long contractorId) {
        return dealRepository.findByContractorIdAndStatus(contractorId, dealStatus.PENDING)
                .stream()
                .map(DealDto::new)
                .collect(Collectors.toList());
    }

    // Создание оригинальной заявки
    public DealDto createDeal(DealDto dto) {
        DealEntity entity = new DealEntity();
        entity.setCustomerId(dto.customerId());
        entity.setProjectName(dto.projectName());
        entity.setPrice(dto.price());
        entity.setMainImage(dto.mainImage());
        entity.setDescription(dto.description());
        entity.setAdditionalImages(dto.additionalImages());
        entity.setStartDate(dto.startDate());
        entity.setEndDate(dto.endDate());
        entity.setServicesIds(dto.servicesIds());
        entity.setStatus(dealStatus.PENDING);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        DealEntity saved = dealRepository.save(entity);
        return new DealDto(saved);
    }

    // Создание отклика (копии сделки для конкретного подрядчика)
    public DealDto createResponse(Long originalDealId, Long contractorId) {
        DealEntity original = dealRepository.findById(originalDealId)
                .orElseThrow(() -> new RuntimeException("Оригинальная заявка не найдена"));

        if (original.getCustomerId() == null) {
            throw new RuntimeException("У оригинальной заявки нет customer_id");
        }

        DealEntity response = new DealEntity();
        response.setCustomerId(original.getCustomerId());
        response.setContractorId(contractorId);
        response.setProjectName(original.getProjectName());
        response.setPrice(original.getPrice());
        response.setMainImage(original.getMainImage());
        response.setDescription(original.getDescription());
        response.setAdditionalImages(original.getAdditionalImages());
        response.setStartDate(original.getStartDate());
        response.setEndDate(original.getEndDate());
        response.setServicesIds(original.getServicesIds());
        response.setStatus(dealStatus.PENDING);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        DealEntity saved = dealRepository.save(response);
        return new DealDto(saved);
    }

    // Подрядчик отправляет сопроводительное письмо
    public void updateContractorMessage(Long dealId, String message) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (deal.getContractorId() == null) {
            throw new RuntimeException("У этой сделки нет подрядчика");
        }

        deal.setContractorMessage(message);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    // Заказчик подтверждает сделку (отменяет все остальные отклики)
    public void confirmDeal(Long selectedDealId) {
        DealEntity selected = dealRepository.findById(selectedDealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (selected.getStatus() != dealStatus.PENDING) {
            throw new RuntimeException("Можно подтвердить только заявку в статусе PENDING");
        }

        if (selected.getContractorId() == null) {
            throw new RuntimeException("У этой сделки нет подрядчика");
        }

        // Находим все PENDING сделки этого заказчика с таким же названием проекта
        List<DealEntity> allDeals = dealRepository.findByCustomerId(selected.getCustomerId());

        for (DealEntity deal : allDeals) {
            if (deal.getStatus() == dealStatus.PENDING
                    && deal.getProjectName().equals(selected.getProjectName())
                    && !deal.getId().equals(selectedDealId)) {
                deal.setStatus(dealStatus.CANCELED);
                deal.setUpdatedAt(LocalDateTime.now());
                dealRepository.save(deal);
            }
        }

        // Подтверждаем выбранную сделку
        selected.setStatus(dealStatus.APPROVED);
        selected.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(selected);
    }

    // ========== МЕТОДЫ ДЛЯ РАБОТЫ СО СТАТУСАМИ ==========

    public void acceptDeal(Long dealId) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (deal.getStatus() != dealStatus.PENDING) {
            throw new RuntimeException("Можно принять только заявку в статусе PENDING");
        }
        if (deal.getContractorId() == null) {
            throw new RuntimeException("Сначала нужно выбрать подрядчика");
        }

        deal.setStatus(dealStatus.APPROVED);
        deal.setCreatedAt(LocalDateTime.now());
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    public void rejectDeal(Long dealId) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (deal.getStatus() != dealStatus.PENDING) {
            throw new RuntimeException("Можно отклонить только заявку в статусе PENDING");
        }

        deal.setContractorId(null);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    public void approveDeal(Long dealId) {
        // 1. Находим подтверждаемую сделку
        DealEntity approvedDeal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (approvedDeal.getStatus() != dealStatus.PENDING) {
            throw new RuntimeException("Можно подтвердить только заявку в статусе PENDING");
        }

        // 2. Находим все PENDING сделки этого заказчика с таким же названием проекта
        List<DealEntity> pendingDeals = dealRepository.findByCustomerIdAndStatus(
                approvedDeal.getCustomerId(), dealStatus.PENDING
        );

        // 3. Отменяем все, кроме подтверждаемой
        for (DealEntity deal : pendingDeals) {
            if (!deal.getId().equals(dealId) && deal.getProjectName().equals(approvedDeal.getProjectName())) {
                deal.setStatus(dealStatus.CANCELED);
                deal.setUpdatedAt(LocalDateTime.now());
                dealRepository.save(deal);
            }
        }

        // 4. Подтверждаем выбранную сделку
        approvedDeal.setStatus(dealStatus.APPROVED);
        approvedDeal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(approvedDeal);
    }

    public void completeDeal(Long dealId) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (deal.getStatus() != dealStatus.APPROVED) {
            throw new RuntimeException("Можно завершить только активную сделку");
        }

        deal.setStatus(dealStatus.COMPLETED);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    public void cancelDeal(Long dealId) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        deal.setStatus(dealStatus.CANCELED);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

    public void selectContractor(Long dealId, Long contractorId) {
        DealEntity deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new RuntimeException("Сделка не найдена"));

        if (deal.getStatus() != dealStatus.PENDING) {
            throw new RuntimeException("Можно выбрать подрядчика только для заявки в статусе PENDING");
        }

        deal.setContractorId(contractorId);
        deal.setUpdatedAt(LocalDateTime.now());
        dealRepository.save(deal);
    }

}