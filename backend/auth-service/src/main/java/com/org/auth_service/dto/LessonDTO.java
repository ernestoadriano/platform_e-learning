package com.org.auth_service.dto;

public record LessonDTO(
        String title,
        String videoUrl,
        Integer duration,
        Long idCourse
) {
}
