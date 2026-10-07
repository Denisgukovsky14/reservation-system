package com.sarcofuckusLesson.designhub.Tables.Customers;

import java.time.LocalDateTime;

public record CustomerDto(
        Long id,
        String name,
        String surname,
        String patronymic,
        String companyName,
        String businessType,
        String address,
        String phone,
        String email,
        String profileImage,
        String about,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public CustomerDto(CustomerEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPatronymic(),
                entity.getCompanyName(),
                entity.getBusinessType(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getProfileImage(),
                entity.getAbout(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}