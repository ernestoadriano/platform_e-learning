package com.ernesto.monolith.assessment.controller;

import com.ernesto.monolith.assessment.model.Option;
import com.ernesto.monolith.assessment.service.QuestionService;
import com.ernesto.monolith.common.dto.CreateOptionDTO;
import com.ernesto.monolith.common.dto.OptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @PostMapping("/{questionId}/options")
    public OptionDTO addOption(@PathVariable("questionId") Long questionId, @RequestBody CreateOptionDTO dto) {
        Option option = service.addOption(questionId, dto);

        return new OptionDTO(option.getQuestionId(), option.getText(), option.isCorrect());
    }
}
