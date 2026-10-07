package com.sarcofuckusLesson.designhub.Tables.Messages;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    // Найти все сообщения по чату (сортировка от старых к новым)
    List<MessageEntity> findByChatIdOrderByCreatedAtAsc(Long chatId);

    // Найти непрочитанные сообщения в чате
    List<MessageEntity> findByChatIdAndIsReadFalse(Long chatId);

    // Найти все сообщения от определенного отправителя в чате
    List<MessageEntity> findByChatIdAndSenderIdAndSenderType(Long chatId, Long senderId, String senderType);
}