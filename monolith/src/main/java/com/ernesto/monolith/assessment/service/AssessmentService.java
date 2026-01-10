package com.ernesto.monolith.assessment.service;

import com.ernesto.monolith.assessment.model.Exam;
import com.ernesto.monolith.assessment.repository.ExamRepository;
import com.ernesto.monolith.enrollment.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

   /* @Autowired
    private ExamRepository examRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    public void submitExam(Long courseId, Long userId, double score) {
        Exam exam = examRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (score >= exam.getPassingScore()) {
            enrollmentService.completeLesson(userId, courseId);
        }
    }*/
}
