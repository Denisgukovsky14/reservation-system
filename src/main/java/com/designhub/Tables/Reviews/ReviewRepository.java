package com.designhub.Tables.Reviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {



    List<ReviewEntity> findByContractorIdAndReceiverTypeOrderByCreatedAtDesc(Long contractorId, String receiverType);


    List<ReviewEntity> findByCustomerIdAndReceiverTypeOrderByCreatedAtDesc(Long customerId, String receiverType);

    // Найти все отзывы по подрядчику (сортировка от новых к старым)
    List<ReviewEntity> findByContractorIdOrderByCreatedAtDesc(Long contractorId);

    boolean existsByCustomerIdAndContractorId(Long customerId, Long contractorId);

    // Найти все отзывы по заказчику
    List<ReviewEntity> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    // Найти отзывы с определенным рейтингом
    List<ReviewEntity> findByContractorIdAndRating(Long contractorId, Integer rating);

    // Найти отзывы по конкретному проекту в портфолио
    List<ReviewEntity> findByPortfolioId(Long portfolioId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.contractorId = :contractorId AND r.receiverType = 'contractor'")
    Double getAverageRatingByContractorId(@Param("contractorId") Long contractorId);

    // Найти все отзывы, которые написал пользователь (где он отправитель)
    @Query("SELECT r FROM ReviewEntity r WHERE (r.customerId = :userId AND r.receiverType = 'contractor') OR (r.contractorId = :userId AND r.receiverType = 'customer')")
    List<ReviewEntity> findReviewsWrittenByUser(@Param("userId") Long userId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.customerId = :customerId AND r.receiverType = 'customer'")
    Double getAverageRatingByCustomerId(@Param("customerId") Long customerId);

    // Подсчитать количество отзывов у подрядчика
    long countByContractorId(Long contractorId);
}
