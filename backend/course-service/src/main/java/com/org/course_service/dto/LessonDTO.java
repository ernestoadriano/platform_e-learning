package com.org.course_service.dto;

public record LessonDTO(
        Long id,
        String title,
        String videoUrl,
        int orderIndex
) {
}
