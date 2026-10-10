package com.designhub.Tables.Chats;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "deal_id", nullable = false)
    private Long dealId;

    @Column(name = "contractor_id", nullable = false)
    private Long contractorId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ChatEntity() {}

    public ChatEntity(Long id, Long dealId, Long contractorId, Long customerId, LocalDateTime createdAt) {
        this.id = id;
        this.dealId = dealId;
        this.contractorId = contractorId;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}