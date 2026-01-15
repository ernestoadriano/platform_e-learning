package com.ernesto.monolith.common.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        String title,
        String message,
        LocalDateTime sentAt
) {
}
