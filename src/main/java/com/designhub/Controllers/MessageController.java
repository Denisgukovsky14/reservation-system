package com.designhub.Controllers;

import com.designhub.Services.MessageService;
import com.designhub.Tables.Messages.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.designhub.Services.ChatService;
import com.designhub.Tables.Messages.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;

    public MessageController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
    }

    // Получить историю сообщений по сделке
    @GetMapping("/deal/{dealId}")
    public ResponseEntity<List<MessageDto>> getMessagesByDealId(@PathVariable Long dealId) {
        try {
            var chat = chatService.getChatByDealId(dealId);
            var messages = messageService.getMessagesByChatId(chat.getId());
            return ResponseEntity.ok(messages.stream().map(MessageDto::new).toList());
        } catch (RuntimeException e) {
            return ResponseEntity.ok(List.of()); // чата ещё нет — возвращаем пустой список
        }
    }

    // Отправить сообщение
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Map<String, Object> body) {
        try {
            Long dealId = Long.valueOf(body.get("dealId").toString());
            String senderType = (String) body.get("senderType");
            Long senderId = Long.valueOf(body.get("senderId").toString());
            String message = (String) body.get("message");

            // Добавляем проверку на null
            Object contractorIdObj = body.get("contractorId");
            Object customerIdObj = body.get("customerId");

            if (contractorIdObj == null || customerIdObj == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "contractorId или customerId не передан"));
            }

            Long contractorId = Long.valueOf(contractorIdObj.toString());
            Long customerId = Long.valueOf(customerIdObj.toString());

            var chat = chatService.getOrCreateChat(dealId, contractorId, customerId);
            var saved = messageService.sendMessage(chat.getId(), senderType, senderId, message);

            return ResponseEntity.ok(Map.of("success", true, "messageId", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }
}
