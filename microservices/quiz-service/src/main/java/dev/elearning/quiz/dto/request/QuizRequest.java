package dev.elearning.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizRequest {
    private String title;
    private String description;
    private Long lessonId;
    private Double passingScore;
    private Integer timeLimitMinutes;
    private Integer maxAttempts;

    private List<QuestionRequest> questions;

}
