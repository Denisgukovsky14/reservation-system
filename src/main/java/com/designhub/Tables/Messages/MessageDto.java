package com.designhub.Tables.Messages;

import java.time.LocalDateTime;
import com.designhub.SenderType;

public record MessageDto(
        Long id,
        Long chatId,
        SenderType senderType,
        Long senderId,
        String message,
        String attachments,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public MessageDto(MessageEntity entity) {
        this(
                entity.getId(),
                entity.getChatId(),
                entity.getSenderType(),
                entity.getSenderId(),
                entity.getMessage(),
                entity.getAttachments(),
                entity.getIsRead(),
                entity.getCreatedAt()
        );
    }
}