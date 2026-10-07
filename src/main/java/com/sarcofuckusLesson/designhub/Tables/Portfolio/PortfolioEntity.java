package com.sarcofuckusLesson.designhub.Tables.Portfolio;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "portfolio")
public class PortfolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contractor_id", nullable = false)
    private Long contractorId;

    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;

    @Column(name = "main_image", length = 255)
    private String mainImage;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "additional_images", columnDefinition = "JSON")
    private String additionalImages;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "client_name", length = 255)
    private String clientName;

    @Column(name = "client_url", length = 255)
    private String clientUrl;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Пустой конструктор
    public PortfolioEntity() {}

    // Конструктор со всеми полями
    public PortfolioEntity(Long id, Long contractorId, String projectName, String mainImage,
                           String description, String additionalImages, LocalDate startDate,
                           LocalDate endDate, String clientName, String clientUrl,
                           Long clientId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.contractorId = contractorId;
        this.projectName = projectName;
        this.mainImage = mainImage;
        this.description = description;
        this.additionalImages = additionalImages;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientName = clientName;
        this.clientUrl = clientUrl;
        this.clientId = clientId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
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
}
