package com.designhub.Tables.Reviews;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "contractor_id", nullable = false)
    private Long contractorId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "portfolio_id")
    private Long portfolioId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "receiver_type", length = 20, nullable = false)
    private String receiverType;

    @Column(name = "deal_id")
    private Long dealId;

    // Пустой конструктор
    public ReviewEntity() {}

    // Конструктор со всеми полями
    public ReviewEntity(Long id, Long customerId, Long contractorId, String title,
                        String comment, Integer rating, Long portfolioId, LocalDateTime createdAt,
                        String receiverType, Long dealId) {
        this.id = id;
        this.customerId = customerId;
        this.contractorId = contractorId;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
        this.portfolioId = portfolioId;
        this.createdAt = createdAt;
        this.receiverType = receiverType;
        this.dealId = dealId;

    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceiverType() { return receiverType; }
    public void setReceiverType(String receiverType) { this.receiverType = receiverType; }

    public Long getDealId() {
        return dealId;
    }

    public void setDealId(Long dealId) {
        this.dealId = dealId;
    }

}
