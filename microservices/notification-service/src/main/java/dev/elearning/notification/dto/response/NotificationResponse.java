package dev.elearning.notification.dto.response;

import dev.elearning.notification.model.enums.NotificationStatus;
import dev.elearning.notification.model.enums.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;
    private Long userId;
    private NotificationType type;
    private String title;
    private String content;
    private Boolean isRead;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private String actionUrl;
    private String imageUrl;
    private NotificationStatus status;
    private String courseTitle;
    private String quizScore;
    private String certificateCode;
    private String moduleName;
}
