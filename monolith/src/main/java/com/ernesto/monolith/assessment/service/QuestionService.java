package com.ernesto.monolith.assessment.service;

import com.ernesto.monolith.assessment.model.Exam;
import com.ernesto.monolith.assessment.model.Option;
import com.ernesto.monolith.assessment.model.Question;
import com.ernesto.monolith.assessment.repository.ExamRepository;
import com.ernesto.monolith.assessment.repository.OptionRepository;
import com.ernesto.monolith.assessment.repository.QuestionRepository;
import com.ernesto.monolith.common.dto.CreateOptionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Option addOption(Long questionId, CreateOptionDTO dto) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        Exam exam = examRepository.findById(question.getExamId())
                .orElseThrow();

        if (exam.isActive()) {
            throw new RuntimeException("Exam already active");
        }

        if (dto.correct() && optionRepository.existsByQuestionIdAndCorrectTrue(questionId)) {
            throw new RuntimeException("Correct option already exists");
        }

        Option option = new Option();
        option.setQuestionId(questionId);
        option.setText(dto.text());
        option.setCorrect(dto.correct());

        return optionRepository.save(option);
    }
}
