package com.sarcofuckusLesson.designhub.Tables.Reviews;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Long customerId,
        Long contractorId,
        String title,
        String comment,
        Integer rating,
        Long portfolioId,
        LocalDateTime createdAt,
        String receiverType,
        Long dealId
) {
    public ReviewDto(ReviewEntity entity) {
        this(
                entity.getId(),
                entity.getCustomerId(),
                entity.getContractorId(),
                entity.getTitle(),
                entity.getComment(),
                entity.getRating(),
                entity.getPortfolioId(),
                entity.getCreatedAt(),
                entity.getReceiverType(),
                entity.getDealId()
        );
    }
}
