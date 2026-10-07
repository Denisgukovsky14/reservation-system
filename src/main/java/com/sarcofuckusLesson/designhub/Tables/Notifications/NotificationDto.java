package com.sarcofuckusLesson.designhub.Tables.Notifications;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        String userType,
        Long userId,
        Long dealId,
        String type,
        String title,
        String content,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public NotificationDto(NotificationEntity entity) {
        this(
                entity.getId(),
                entity.getUserType(),
                entity.getUserId(),
                entity.getDealId(),
                entity.getType(),
                entity.getTitle(),
                entity.getContent(),
                entity.getIsRead(),
                entity.getCreatedAt()
        );
    }
}
