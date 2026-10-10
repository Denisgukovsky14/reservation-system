package com.designhub.Tables.Notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    // Найти все уведомления по пользователю
    List<NotificationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Найти все уведомления по пользователю и типу
    List<NotificationEntity> findByUserIdAndType(Long userId, String type);

    // Найти непрочитанные уведомления пользователя
    List<NotificationEntity> findByUserIdAndIsReadFalse(Long userId);

    // Найти все уведомления по сделке
    List<NotificationEntity> findByDealIdOrderByCreatedAtDesc(Long dealId);

    // Подсчитать количество непрочитанных уведомлений пользователя
    long countByUserIdAndIsReadFalse(Long userId);
}
