package com.designhub.Tables.Deals;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.designhub.dealStatus;

public interface DealRepository extends JpaRepository<DealEntity, Long> {

    // Найти все сделки по подрядчику
    List<DealEntity> findByContractorId(Long contractorId);

    // Найти все сделки по заказчику
    List<DealEntity> findByCustomerId(Long customerId);

    // Найти все сделки по статусу
    List<DealEntity> findByStatus(dealStatus status);

    // Найти сделки подрядчика по статусу
    List<DealEntity> findByContractorIdAndStatus(Long contractorId, dealStatus status);

    // Найти сделки заказчика по статусу
    List<DealEntity> findByCustomerIdAndStatus(Long customerId, dealStatus status);
}
