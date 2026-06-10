package dev.elearning.progress.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseProgressResponse {

    private Long courseId;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double percentage;
    private Boolean isCompleted;
    private LocalDateTime lastAccessedAt;
    private List<ModuleProgressResponse> modules;
}
