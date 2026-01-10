package com.org.auth_service.dto;

import com.org.auth_service.model.enums.EnrollmentStatus;

public record EnrollmentDTO(
        Long idStudent,
        Long idCourse,
        EnrollmentStatus status,
        Double progress
) {
}
