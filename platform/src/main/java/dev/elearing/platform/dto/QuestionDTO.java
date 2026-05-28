package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionDTO {

    private Long id;
    private String text;
    private List<OptionDTO> options;
    private Integer correctAnswer;
    private String explanation;
}
