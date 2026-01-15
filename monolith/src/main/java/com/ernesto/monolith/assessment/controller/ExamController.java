package com.ernesto.monolith.assessment.controller;

import com.ernesto.monolith.assessment.model.ExamAttempt;
import com.ernesto.monolith.assessment.service.ExamService;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService service;

    @PostMapping("/{examId}/submit")
    public ExamAttempt submitExam(@PathVariable("examId") Long examId, @RequestBody Map<Long, Long> answers, @AuthenticationPrincipal User user) {
        return service.submitExam(examId, answers, user);
    }
}
