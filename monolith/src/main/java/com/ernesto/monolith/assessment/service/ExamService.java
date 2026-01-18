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
import com.ernesto.monolith.common.dto.AnswerDTO;
import com.ernesto.monolith.common.dto.CreateQuestionDTO;
import com.ernesto.monolith.common.dto.SubmitExamDTO;
import com.ernesto.monolith.course.model.Module;
import com.ernesto.monolith.course.repository.ModuleRepository;
import com.ernesto.monolith.enrollment.model.Enrollment;
import com.ernesto.monolith.enrollment.repository.EnrollmentRepository;
import com.ernesto.monolith.enrollment.service.EnrollmentService;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;


    public Exam createFinalExam(Long courseId, User instructor) {
        Module module = moduleRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Exam exam = new Exam();

        exam.setModuleId(module.getId());
        exam.setCourseId(module.getCourseId());
        exam.setType(ExamType.FINAL_EXAM);
        return examRepository.save(exam);
    }

    public Exam createModuleTest(Long moduleId) {

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Exam exam = new Exam();

        exam.setModuleId(moduleId);
        exam.setCourseId(module.getCourseId());
        exam.setType(ExamType.MODULE_TEST);
        return examRepository.save(exam);
    }

    public Exam createQuiz(Long courseId, User instructor) {
        Module module = moduleRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Exam exam = new Exam();

        exam.setModuleId(module.getId());
        exam.setCourseId(module.getCourseId());
        exam.setType(ExamType.QUIZ);
        return examRepository.save(exam);
    }

    public Question addQuestion(Long examId, CreateQuestionDTO dto) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow();
        if (exam.isActive()) {
            throw new RuntimeException("Exam already active");
        }

        Question question = new Question();

        question.setExamId(examId);
        question.setStatement(dto.statement());

        return questionRepository.save(question);
    }
    public ExamAttempt submitExam(Long examId, SubmitExamDTO dto, User student) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (!exam.isActive()) {
            throw new RuntimeException("Exam not active");
        }

        List<Question> questions = questionRepository.findAllByExamId(examId);

        int correct = 0;


        for (Question question : questions) {
            AnswerDTO answer = dto.answers().stream()
                    .filter(a ->
                            a.questionId().equals(question.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Answer missing"));

            Option option = optionRepository.findById(answer.optionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            if (option.isCorrect()) {
                correct++;
            }
        }

        double score = (double) correct / questions.size() * 100;

        boolean passed = score >= 65;

        ExamAttempt attempt = new ExamAttempt();

        attempt.setStudentId(student.getId());
        attempt.setExamId(exam.getId());
        attempt.setScore(score);
        attempt.setPassed(passed);

        examAttemptRepository.save(attempt);

        if (passed) {
            handleProgressAfterExam(exam, student);
        }
        return attempt;
    }

    public void activeExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow();

        boolean hasQuestions = questionRepository.existsByExamId(examId);

        if (!hasQuestions) {
            throw new RuntimeException("Exam has no questions");
        }

        exam.setActive(true);
        examRepository.save(exam);
    }

    public boolean isFinalExamPassed(User student, Long courseId) {
        return examAttemptRepository.existsByStudentIdAndCourseIdAndPassedTrue(student.getId(), courseId);
    }

    public boolean isModuleTestPassed(User student, Long moduleId) {
        return examAttemptRepository.existsByStudentIdAndModuleIdAndPassedTrue(student.getId(), moduleId);
    }

    public boolean isQuizTestPassed(User student, Long lessonId) {
        return examAttemptRepository.existsByStudentIdAndLessonIdAndPassedTrue(student.getId(), lessonId);
    }

    private void handleProgressAfterExam(Exam exam, User student) {
        if (exam.getType().equals(ExamType.MODULE_TEST)) {
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), exam.getCourseId())
                            .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            enrollmentService.markModuleCompleted(enrollment, exam.getModuleId());
        } else if (exam.getType().equals(ExamType.FINAL_EXAM)) {
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), exam.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            enrollmentService.markCourseCompleted(enrollment, student);
        } else if (exam.getType().equals(ExamType.QUIZ)) {
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), exam.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Enrollment not found"));
            enrollmentService.markLessonCompleted(enrollment.getId(), exam.getLessonId(), student);
        }
    }
}
