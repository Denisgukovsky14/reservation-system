package com.sarcofuckusLesson.designhub.Tables.Deals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.sarcofuckusLesson.designhub.dealStatus;

public record DealDto(
        Long id,
        Long contractorId,
        Long customerId,
        String projectName,
        Integer price,
        String mainImage,
        String description,
        String additionalImages,
        LocalDate startDate,
        LocalDate endDate,
        dealStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String servicesIds,
        String contractorMessage
) {
    public DealDto(DealEntity entity) {
        this(
                entity.getId(),
                entity.getContractorId(),
                entity.getCustomerId(),
                entity.getProjectName(),
                entity.getPrice(),
                entity.getMainImage(),
                entity.getDescription(),
                entity.getAdditionalImages(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getServicesIds(),
                entity.getContractorMessage()
        );
    }
}