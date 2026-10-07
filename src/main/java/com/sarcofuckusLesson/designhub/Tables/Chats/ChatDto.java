package com.sarcofuckusLesson.designhub.Tables.Chats;

import java.time.LocalDateTime;

public record ChatDto(
        Long id,
        Long dealId,
        Long contractorId,
        Long customerId,
        LocalDateTime createdAt
) {
    public ChatDto(ChatEntity entity) {
        this(
                entity.getId(),
                entity.getDealId(),
                entity.getContractorId(),
                entity.getCustomerId(),
                entity.getCreatedAt()
        );
    }
}