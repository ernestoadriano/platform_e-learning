package dev.elearning.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationResponse {
    private Long notificationId;
    private Boolean success;
    private String message;
}
