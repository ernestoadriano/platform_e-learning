package com.ernesto.monolith.assessment.service;

import com.ernesto.monolith.assessment.model.Exam;
import com.ernesto.monolith.assessment.model.ExamAttempt;
import com.ernesto.monolith.assessment.model.Option;
import com.ernesto.monolith.assessment.model.Question;
import com.ernesto.monolith.assessment.model.enums.ExamType;
import com.ernesto.monolith.assessment.repository.ExamAttemptRepository;
import com.ernesto.monolith.assessment.repository.ExamRepository;
import com.ernesto.monolith.assessment.repository.OptionRepository;
import com.ernesto.monolith.assessment.repository.QuestionRepository;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Course;
import com.ernesto.monolith.course.model.Module;
import com.ernesto.monolith.course.repository.ModuleRepository;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExamService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;



    public Exam createModuleTest(Long moduleId, User instructor) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Exam exam = new Exam();

        exam.setModuleId(moduleId);
        exam.setCourseId(module.getCourseId());
        exam.setType(ExamType.MODULE_TEST);
        return examRepository.save(exam);
    }

    public Exam createFinalExam(Long courseId, User instructor) {
        Module module = moduleRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Exam exam = new Exam();

        exam.setModuleId(module.getId());
        exam.setCourseId(module.getCourseId());
        exam.setType(ExamType.FINAL_EXAM);
        return examRepository.save(exam);
    }



    public ExamAttempt submitExam(Long examId, Map<Long, Long> answers, User student) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not active"));

        List<Question> questions = questionRepository.findAllByExamId(exam.getId());

        int correct = 0;

        for (Question question : questions) {
            Long selectedOption = answers.get(question.getId());
            Option option = optionRepository.findById(selectedOption)
                    .orElseThrow();
            if (option.isCorrect())
                correct++;
        }

        double score = (double) correct / questions.size() * 100;

        boolean passed = score >= 60;
        ExamAttempt examAttempt = new ExamAttempt();
        examAttempt.setStudentId(student.getId());
        examAttempt.setExamId(examId);
        examAttempt.setScore(score);
        examAttempt.setPassed(passed);

        examAttemptRepository.save(examAttempt);

        return examAttempt;
    }

    public void activeExam(Long examId, User instructor) {

    }

    public boolean finalExamPassed(User student, Long courseId) {
        return examAttemptRepository.existsByStudentIdAndCourseIdAndPassedTrue(student.getId(), courseId);
    }

    public boolean moduleTestPassed(User student, Long moduleId) {
        return examAttemptRepository.existsByStudentIdAndModuleIdAndPassedTrue(student.getId(), moduleId);
    }
}
