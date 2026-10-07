package com.sarcofuckusLesson.designhub.Tables.Customers;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "surname", length = 100, nullable = false)
    private String surname;

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

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "profile_image", length = 500)
    private String profileImage;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    // Пустой конструктор
    public CustomerEntity() {}

    // Конструктор со всеми полями (без дат, они заполнятся автоматически)
    public CustomerEntity(Long id, String name, String surname, String patronymic,
                          String companyName, String businessType, String address,
                          String phone, String email, String profileImage,
                          String about, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.companyName = companyName;
        this.businessType = businessType;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.profileImage = profileImage;
        this.about = about;
        this.password = password;
    }

    // Автоматическое заполнение created_at и updated_at при сохранении
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    // Автоматическое обновление updated_at при изменении
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getPatronymic() { return patronymic; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}