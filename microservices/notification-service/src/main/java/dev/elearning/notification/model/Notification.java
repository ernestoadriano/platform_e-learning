package dev.elearning.notification.model;

import dev.elearning.notification.model.enums.NotificationStatus;
import dev.elearning.notification.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    private LocalDateTime sentAt;

    @Column(length = 500)
    private String actionUrl;

    @Column(length = 500)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private String courseTitle;
    private String quizScore;
    private String certificateCode;
    private String moduleName;
}
