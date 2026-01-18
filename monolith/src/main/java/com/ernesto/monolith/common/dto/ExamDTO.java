package com.ernesto.monolith.common.dto;

public record ExamDTO(
        Long courseId,
        Long moduleId,
        String type
) {
}
