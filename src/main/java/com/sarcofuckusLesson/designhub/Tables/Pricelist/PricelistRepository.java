package com.sarcofuckusLesson.designhub.Tables.Pricelist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PricelistRepository extends JpaRepository<PricelistEntity, Long> {

    // Найти все цены по подрядчику
    List<PricelistEntity> findByContractorId(Long contractorId);

    // Найти все цены по услуге
    List<PricelistEntity> findByServiceId(Long serviceId);

    // Найти цену конкретной услуги у конкретного подрядчика
    Optional<PricelistEntity> findByServiceIdAndContractorId(Long serviceId, Long contractorId);

    // Удалить все цены подрядчика (при удалении подрядчика)
    void deleteByContractorId(Long contractorId);

    // Удалить все цены на услугу (при удалении услуги)
    void deleteByServiceId(Long serviceId);
}
