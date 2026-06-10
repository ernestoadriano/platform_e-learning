package dev.elearning.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizWithQuestionsResponse {

    private Long id;
    private String title;
    private String description;
    private Double passingScore;
    private Integer timeLimitMinutes;
    private List<QuestionResponse> questions;
}
