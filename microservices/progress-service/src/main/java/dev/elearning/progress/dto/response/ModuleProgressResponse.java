package dev.elearning.progress.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModuleProgressResponse {

    private Long moduleId;
    private Integer completedLessons;
    private Integer totalLessons;
    private Double percentage;
    private Boolean isCompleted;
    private Boolean examPassed;
    private Double examScore;
}
