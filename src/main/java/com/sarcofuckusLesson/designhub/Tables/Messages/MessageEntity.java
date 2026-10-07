package com.sarcofuckusLesson.designhub.Tables.Messages;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.sarcofuckusLesson.designhub.SenderType;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", length = 20, nullable = false)
    private SenderType senderType;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "attachments", columnDefinition = "JSON")
    private String attachments;  // JSON строка, например '["file1.jpg", "file2.pdf"]'

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Пустой конструктор
    public MessageEntity() {}

    // Конструктор со всеми полями
    public MessageEntity(Long id, Long chatId, SenderType senderType, Long senderId,
                         String message, String attachments, Boolean isRead,
                         LocalDateTime createdAt) {
        this.id = id;
        this.chatId = chatId;
        this.senderType = senderType;
        this.senderId = senderId;
        this.message = message;
        this.attachments = attachments;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public void setSenderType(SenderType senderType) {
        this.senderType = senderType;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}