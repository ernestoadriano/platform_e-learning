package dev.elearning.notification.dto.request;

import dev.elearning.notification.model.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationRequest {

    private Long userId;
    private NotificationType type;
    private String title;
    private String content;
    private String actionUrl;
    private String imageUrl;
    private String courseTitle;
    private String quizScore;
    private String certificateCode;
    private String moduleName;
}
