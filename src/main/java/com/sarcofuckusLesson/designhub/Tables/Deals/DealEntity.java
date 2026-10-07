package com.sarcofuckusLesson.designhub.Tables.Deals;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.sarcofuckusLesson.designhub.dealStatus;

@Entity
@Table(name = "deals")
public class DealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contractor_id")
    private Long contractorId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "main_image", length = 255)
    private String mainImage;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "additional_images", columnDefinition = "TEXT")
    private String additionalImages;  // можно хранить как JSON строку

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private dealStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "services_ids", length = 255)
    private String servicesIds;

    @Column(name = "contractor_message", columnDefinition = "TEXT")
    private String contractorMessage;

    // Пустой конструктор
    public DealEntity() {}

    // Конструктор со всеми полями
    public DealEntity(Long id, Long contractorId, Long customerId, String projectName,
                      Integer price, String mainImage, String description, String additionalImages,
                      LocalDate startDate, LocalDate endDate, dealStatus status,
                      LocalDateTime createdAt, LocalDateTime updatedAt, String servicesIds) {
        this.id = id;
        this.contractorId = contractorId;
        this.customerId = customerId;
        this.projectName = projectName;
        this.price = price;
        this.mainImage = mainImage;
        this.description = description;
        this.additionalImages = additionalImages;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.servicesIds = servicesIds;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(String additionalImages) {
        this.additionalImages = additionalImages;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public dealStatus getStatus() {
        return status;
    }

    public void setStatus(dealStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(String servicesIds) {
        this.servicesIds = servicesIds;
    }

    public String getContractorMessage() { return contractorMessage; }
    public void setContractorMessage(String contractorMessage) { this.contractorMessage = contractorMessage; }

}