package com.ernesto.monolith.common.dto;

public record LessonDTO(
        Long id,
        String title,
        String videoUrl,
        int orderIndex
) {
}
