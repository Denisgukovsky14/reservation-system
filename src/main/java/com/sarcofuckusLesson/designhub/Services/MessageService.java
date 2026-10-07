package com.sarcofuckusLesson.designhub.Services;

import com.sarcofuckusLesson.designhub.SenderType;
import com.sarcofuckusLesson.designhub.Tables.Messages.MessageEntity;
import com.sarcofuckusLesson.designhub.Tables.Messages.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }



    public MessageEntity sendMessage(Long chatId, String senderTypeStr, Long senderId, String message) {
        MessageEntity entity = new MessageEntity();
        entity.setChatId(chatId);

        // Преобразуем строку из нижнего регистра в Enum (верхний регистр)
        SenderType senderType = SenderType.valueOf(senderTypeStr.toUpperCase());
        entity.setSenderType(senderType);

        entity.setSenderId(senderId);
        entity.setMessage(message);
        return messageRepository.save(entity);
    }

    public List<MessageEntity> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtAsc(chatId);
    }
}