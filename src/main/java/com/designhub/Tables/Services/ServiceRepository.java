package com.designhub.Tables.Services;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    // Найти услугу по имени
    Optional<ServiceEntity> findByName(String name);

    // Проверить, существует ли услуга с таким именем
    boolean existsByName(String name);

    // Найти все услуги, имя которых содержит подстроку (для поиска)
    List<ServiceEntity> findByNameContainingIgnoreCase(String name);
}