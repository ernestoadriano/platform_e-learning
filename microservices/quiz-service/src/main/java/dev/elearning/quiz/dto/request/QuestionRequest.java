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
public class QuestionRequest {

    private String text;
    private Integer correactAnswer;
    private String explanation;
    private List<String> options;
}
