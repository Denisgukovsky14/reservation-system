package com.designhub.Tables.Services;

public record ServiceDto(
        Long id,
        String name,
        String description,
        String imageUrl,
        String tag
) {
    public ServiceDto(ServiceEntity entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getTag()
        );
    }
}