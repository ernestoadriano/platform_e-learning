package dev.elearning.lesson.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonProgressResponse {

    private Long id;
    private Long userId;
    private Long lessonId;
    private Boolean isCompleted;
    private Integer score;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;
}
