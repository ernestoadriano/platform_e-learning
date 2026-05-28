package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExamQuestionDTO {
    private Long id;
    private String text;
    private List<Map<String, Object>> options;
}
