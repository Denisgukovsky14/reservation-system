package com.designhub.Tables.Chats;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    // Найти все чаты по сделке
    Optional<ChatEntity> findByDealId(Long dealId);

    // Найти все чаты по подрядчику
    List<ChatEntity> findByContractorId(Long contractorId);

    // Найти все чаты по заказчику
    List<ChatEntity> findByCustomerId(Long customerId);
}