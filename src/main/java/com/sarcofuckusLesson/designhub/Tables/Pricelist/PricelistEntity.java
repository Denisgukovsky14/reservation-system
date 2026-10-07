package com.sarcofuckusLesson.designhub.Tables.Pricelist;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pricelist")
public class PricelistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "contractor_id", nullable = false)
    private Long contractorId;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Пустой конструктор
    public PricelistEntity() {}

    // Конструктор со всеми полями
    public PricelistEntity(Long id, Long serviceId, Long contractorId, Integer price, LocalDateTime createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.contractorId = contractorId;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
