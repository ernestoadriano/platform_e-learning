package dev.elearning.quiz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuizAttemptResponse {

    private Long id;
    private Integer attemptNumber;
    private Integer score;
    private Boolean passed;
    private LocalDateTime completedAt;
}
