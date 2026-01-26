package com.org.course_service.dto;

import lombok.NonNull;

public record UpdateModuleDTO(
        @NonNull String title
) {
}
