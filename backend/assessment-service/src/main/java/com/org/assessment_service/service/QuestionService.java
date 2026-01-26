package com.org.assessment_service.service;

import com.org.assessment_service.dto.CreateOptionDTO;
import com.org.assessment_service.model.Exam;
import com.org.assessment_service.model.Option;
import com.org.assessment_service.model.Question;
import com.org.assessment_service.repository.ExamRepository;
import com.org.assessment_service.repository.OptionRepository;
import com.org.assessment_service.repository.QuestionRepository;
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

        Exam exam = examRepository.findById(question.getExam().getId())
                .orElseThrow();

        if (exam.isActive()) {
            throw new RuntimeException("Exam already active");
        }

        if (dto.correct() && optionRepository.existsByQuestionIdAndCorrectTrue(questionId)) {
            throw new RuntimeException("Correct option already exists");
        }

        Option option = new Option();
        option.setQuestion(question);
        option.setText(dto.text());
        option.setCorrect(dto.correct());

        return optionRepository.save(option);
    }
}
