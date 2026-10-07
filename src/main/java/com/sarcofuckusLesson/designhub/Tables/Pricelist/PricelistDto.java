package com.sarcofuckusLesson.designhub.Tables.Pricelist;

import java.time.LocalDateTime;

public record PricelistDto(
        Long id,
        Long serviceId,
        Long contractorId,
        Integer price,
        LocalDateTime createdAt
) {
    public PricelistDto(PricelistEntity entity) {
        this(
                entity.getId(),
                entity.getServiceId(),
                entity.getContractorId(),
                entity.getPrice(),
                entity.getCreatedAt()
        );
    }
}
