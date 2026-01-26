package com.org.course_service.dto;

public record CreateLessonDTO(
        Long moduleId,
        String title,
        String videoUrl
) {
}
