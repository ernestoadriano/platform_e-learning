package com.ernesto.monolith.assessment.controller;

import com.ernesto.monolith.assessment.model.ExamAttempt;
import com.ernesto.monolith.assessment.model.Question;
import com.ernesto.monolith.assessment.service.ExamService;
import com.ernesto.monolith.common.dto.*;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService service;

    @PostMapping("/{examId}/submit")
    public ExamAttemptDTO submitExam(@PathVariable("examId") Long examId, @RequestBody SubmitExamDTO dto, @AuthenticationPrincipal User student) {
        ExamAttempt result = service.submitExam(examId, dto, student);
        return new ExamAttemptDTO(result.getScore(), result.getPassed());
    }

    @PostMapping("/{courseId}/exam")
    public ExamDTO createFinalExam(@PathVariable("courseId") Long courseId, @AuthenticationPrincipal User instructor) {
        var exam = service.createFinalExam(courseId, instructor);

        return new ExamDTO(exam.getCourseId(), exam.getModuleId(), exam.getType().getType());
    }

    @PostMapping("/{moduleId}/test")
    public ExamDTO createModuleTest(@PathVariable("moduleId") Long moduleId) {
        var exam = service.createModuleTest(moduleId);
        return new ExamDTO(exam.getCourseId(), exam.getModuleId(), exam.getType().getType());
    }

    @PostMapping("/{courseId}/quiz")
    public ExamDTO createQuiz(@PathVariable("lesson") Long lessonId, @AuthenticationPrincipal User instructor) {
        var exam = service.createQuiz(lessonId, instructor);

        return new ExamDTO(exam.getCourseId(), exam.getModuleId(), exam.getType().getType());
    }

    @PostMapping("/{examId}/questions")
    public QuestionDTO addQuestion(@PathVariable("examId") Long examId, @RequestBody CreateQuestionDTO dto) {
        Question question = service.addQuestion(examId, dto);

        return new QuestionDTO(question.getStatement(), question.getExamId());
    }

    @PostMapping("/{examId}/activate")
    public void activeExam(@PathVariable("examId") Long examId) {
        service.activeExam(examId);
    }
}
