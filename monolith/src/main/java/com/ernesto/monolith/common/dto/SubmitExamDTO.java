package com.ernesto.monolith.common.dto;

import java.util.List;

public record SubmitExamDTO(
        List<AnswerDTO> answers
) {
}
