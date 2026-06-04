package dev.elearning.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnrollmentResponse {

    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDateTime enrolledAt;
    private LocalDateTime expiresAt;
    private Boolean active;
    private String courseTitle;
    private String courseImageUrl;
}
