package dev.elearning.notification.repository;

import dev.elearning.notification.model.Notification;
import dev.elearning.notification.model.enums.NotificationStatus;
import dev.elearning.notification.model.enums.NotificationType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Notification> findAllByUserId(Long userId, Pageable pageable);

    List<Notification> findAllByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndIsReadFalse(Long userId);

    List<Notification> findAllByUserIdAndType(Long userId, NotificationType type);

    List<Notification> findAllByStatusAndSentAtIsNull(NotificationStatus status);

    List<Notification> findStatus(NotificationStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true, n.readtAt = CURRENT_TIMESTAMP WHERE n.userId = :userId AND n.id = :notificationId")
    void markAsRead(@Param("userId") Long userId, @Param("notificationId") Long notificationId);

    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.userId = :userId")
    void markAllAsRead(@Param("userId") Long userId);

    void updateStatus(@Param("id") Long id, @Param("status") NotificationStatus status);

    @Modifying
    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime date);

    @Modifying
    @Transactional
    void deleteByUserId(Long userId);
}
