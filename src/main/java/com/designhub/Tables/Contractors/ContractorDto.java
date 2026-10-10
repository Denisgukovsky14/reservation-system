package com.designhub.Tables.Contractors;

import java.time.LocalDateTime;

public record ContractorDto(
        Long id,
        String firstName,
        String lastName,
        String patronymic,
        String companyName,
        String businessType,
        String address,
        String phone,
        String email,
        String profileImage,
        String aboutCompany,
        String servicesIds,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public ContractorDto(ContractorEntity entity) {
        this(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPatronymic(),
                entity.getCompanyName(),
                entity.getBusinessType(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getProfileImage(),
                entity.getAboutCompany(),
                entity.getServicesIds(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}