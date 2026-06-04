package dev.elearning.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizResponse {
    private Long id;
    private String title;
    private String description;
    private Long lessonId;
    private Integer passingScore;
    private Integer maxAttempts;
}
