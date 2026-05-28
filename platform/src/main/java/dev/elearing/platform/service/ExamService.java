package dev.elearing.platform.service;

import dev.elearing.platform.dto.ExamQuestionDTO;
import dev.elearing.platform.dto.ExamSubmitRequest;
import dev.elearing.platform.model.*;
import dev.elearing.platform.model.Module;
import dev.elearing.platform.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ModuleExamRepository moduleExamRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private ExamAnswerRepository examAnswerRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<ExamQuestionDTO> getModuleExam(Long moduleId) {
        ModuleExam exam = moduleExamRepository.findByModuleId(moduleId)
                .orElseThrow(() -> new RuntimeException("Exam not found for this module"));

        List<ExamQuestion> questions = examQuestionRepository.findByExamIdOrderById(exam.getId());

        return questions.stream().map(q -> {
            ExamQuestionDTO dto = new ExamQuestionDTO();
            dto.setId(q.getId());
            dto.setText(q.getText());

            List<Map<String, Object>> options = q.getOptions().stream()
                    .map(opt -> {
                        Map<String, Object> optMap = new HashMap<>();
                        optMap.put("id", opt.getId());
                        optMap.put("text", opt.getText());
                        optMap.put("order", opt.getOptionOrder());
                        return optMap;
                    })
                    .toList();
            dto.setOptions(options);
            return dto;
        }).toList();
    }

    @Transactional
    public Map<String, Object> submitExam(Long userId, Long moduleId, ExamSubmitRequest request) {
        ModuleExam exam = moduleExamRepository.findByModuleId(moduleId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        List<ExamQuestion> questions = examQuestionRepository.findByExamIdOrderById(exam.getId());
        List<Integer> userAnswers = request.getAnswers();

        ExamAttempt attempt = new ExamAttempt();
        attempt.setUserId(userId);

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        attempt.setModule(module);

        int correct = 0;

        List<ExamAnswer> examAnswers = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            ExamQuestion question = questions.get(i);
            Integer userAnswer = i < userAnswers.size() ? userAnswers.get(i) : 1;

            boolean isCorrect = userAnswer.equals(question.getCorrectAnswer());

            if (isCorrect) correct++;

            ExamAnswer answer = new ExamAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(question);
            answer.setSelectedOption(userAnswer);
            answer.setIsCorrect(isCorrect);
            examAnswers.add(answer);
        }

        int score = (correct * 100) / questions.size();

        boolean passed = score >= exam.getPassingScore();

        attempt.setScore(score);
        attempt.setPassed(passed);
        attempt = examAttemptRepository.save(attempt);

        for (ExamAnswer answer : examAnswers) {
            answer.setAttempt(attempt);
            examAnswerRepository.save(answer);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("passed", passed);
        result.put("message", passed ? "Congratulations! You passed the exam!" : "You need " + exam.getPassingScore() + "% to pass. Please try again.");

        return result;
    }

    public boolean canAccessModule(Long userId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (module.getOrder() == 0) {
            return true;
        }

        Module previousModule = moduleRepository.findByCourseIdAndModuleOrder(module.getCourse().getId(), module.getOrder() - 1)
                .orElse(null);

        if (previousModule == null) return true;

        return examAttemptRepository.existsByUserIdAndModuleIdAndPassed(userId, previousModule.getId(), true);
    }
}
