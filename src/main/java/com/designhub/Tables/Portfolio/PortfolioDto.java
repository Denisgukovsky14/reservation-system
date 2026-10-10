package com.designhub.Tables.Portfolio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PortfolioDto(
        Long id,
        Long contractorId,
        String projectName,
        String mainImage,
        String description,
        String additionalImages,
        LocalDate startDate,
        LocalDate endDate,
        String clientName,
        String clientUrl,
        Long clientId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public PortfolioDto(PortfolioEntity entity) {
        this(
                entity.getId(),
                entity.getContractorId(),
                entity.getProjectName(),
                entity.getMainImage(),
                entity.getDescription(),
                entity.getAdditionalImages(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getClientName(),
                entity.getClientUrl(),
                entity.getClientId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
