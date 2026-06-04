package dev.elearning.lesson.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonProgressRequest {

    private Long userId;
    private Long lessonId;
    private Integer score;
}
