package dev.elearning.notification.dto.response;

import lombok.Data;

@Data
public class UnreadCount {
    private Long userId;
    private Long unreadCount;
}
