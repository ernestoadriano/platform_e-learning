package com.ernesto.monolith.common.dto;

public record CreateLessonDTO (
     Long moduleId,
     String title,
     String videoUrl
) {
}
