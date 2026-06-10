package dev.elearning.certificate.dto.response;

import lombok.Data;

@Data
public class CourseProgressResponse {
    private Long courseId;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double percentage;
    private Boolean isCompleted;
}
