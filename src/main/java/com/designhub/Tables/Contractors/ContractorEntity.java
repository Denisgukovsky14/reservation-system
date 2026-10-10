package com.designhub.Tables.Contractors;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contractors")
public class ContractorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "patronymic", length = 100)
    private String patronymic;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "business_type", length = 50)
    private String businessType;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    @Column(name = "about_company", columnDefinition = "TEXT")
    private String aboutCompany;

    @Column(name = "services_ids", length = 255)
    private String servicesIds;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    // Пустой конструктор (обязательно для JPA)
    public ContractorEntity() {}

    // Конструктор со всеми полями
    public ContractorEntity(Long id, String firstName, String lastName, String patronymic,
                            String companyName, String businessType, String address,
                            String phone, String email, String profileImage,
                            String aboutCompany, String servicesIds,
                            LocalDateTime createdAt, LocalDateTime updatedAt, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.companyName = companyName;
        this.businessType = businessType;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.profileImage = profileImage;
        this.aboutCompany = aboutCompany;
        this.servicesIds = servicesIds;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.password = password;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getAboutCompany() {
        return aboutCompany;
    }

    public void setAboutCompany(String aboutCompany) {
        this.aboutCompany = aboutCompany;
    }

    public String getServicesIds() {
        return servicesIds;
    }

    public void setServicesIds(String servicesIds) {
        this.servicesIds = servicesIds;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}