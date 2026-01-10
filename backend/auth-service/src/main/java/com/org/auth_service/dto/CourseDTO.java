package com.org.auth_service.dto;

public record CourseDTO(
        String title,
        String description,
        Double price,
        String instructorName,
        Long idInstructor
) {
}
