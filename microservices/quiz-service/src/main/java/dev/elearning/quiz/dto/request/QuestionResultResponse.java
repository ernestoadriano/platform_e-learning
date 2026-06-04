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
public class QuestionResultResponse {
    private Long attemptId;
    private Integer attemptNumber;
    private Integer score;
    private Boolean passed;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private String message;
    private List<Integer> answers;
    private List<Boolean> correctAnswersList;
}
