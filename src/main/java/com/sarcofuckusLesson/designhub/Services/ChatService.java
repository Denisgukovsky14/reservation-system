package com.sarcofuckusLesson.designhub.Services;

import com.sarcofuckusLesson.designhub.Tables.Chats.ChatEntity;
import com.sarcofuckusLesson.designhub.Tables.Chats.ChatRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public ChatEntity getOrCreateChat(Long dealId, Long contractorId, Long customerId) {
        return chatRepository.findByDealId(dealId)
                .orElseGet(() -> {
                    ChatEntity chat = new ChatEntity();
                    chat.setDealId(dealId);
                    chat.setContractorId(contractorId);
                    chat.setCustomerId(customerId);
                    return chatRepository.save(chat);
                });
    }

    public ChatEntity getChatByDealId(Long dealId) {
        return chatRepository.findByDealId(dealId)
                .orElseThrow(() -> new RuntimeException("Чат не найден для сделки " + dealId));
    }
}
