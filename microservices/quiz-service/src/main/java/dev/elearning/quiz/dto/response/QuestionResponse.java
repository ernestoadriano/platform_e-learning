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
public class QuestionResponse {

    private Long id;
    private String text;
    private Integer questionOrder;
    private List<String> options;
}
