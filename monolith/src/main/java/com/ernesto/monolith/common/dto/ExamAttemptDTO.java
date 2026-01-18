package com.ernesto.monolith.common.dto;

public record ExamAttemptDTO(
        double score,
        boolean passed
) {
}
