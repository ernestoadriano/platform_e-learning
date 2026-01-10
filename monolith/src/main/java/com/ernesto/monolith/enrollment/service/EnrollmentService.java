package com.ernesto.monolith.enrollment.service;

import com.ernesto.monolith.certificate.service.CertificateService;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Lesson;
import com.ernesto.monolith.course.repository.LessonRepository;
import com.ernesto.monolith.enrollment.model.Enrollment;
import com.ernesto.monolith.enrollment.model.LessonProgress;
import com.ernesto.monolith.enrollment.model.enums.EnrollmentStatus;
import com.ernesto.monolith.enrollment.repository.EnrollmentRepository;
import com.ernesto.monolith.enrollment.repository.LessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {


    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Enrollment getActiveEnrollment(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new BusinessException("Enrollment not found"));
        if (!enrollment.getStatus().equals(EnrollmentStatus.ACTIVE)) {
            throw new BusinessException("This course is already complete");
        }

        return enrollment;
    }

    public boolean isNextLesson(Enrollment enrollment, Lesson lesson) {
        return true;
    }

    public boolean isLastLessonOfModule(Enrollment enrollment, Lesson lesson) {
        return true;
    }

    public boolean allLessonsCompleted(Enrollment enrollment, Long moduleId) {
        return true;
    }
    /*@Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CertificateService certificateService;

    public void completeLesson(Long enrollmentId, Long lessonId) {

        if (progressRepository.existsByEnrollmentIdAndLessonId(enrollmentId, lessonId)) {
            return;
        }

        LessonProgress lessonProgress = new LessonProgress();
        lessonProgress.setEnrollmentId(enrollmentId);
        lessonProgress.setLessonId(lessonId);
        lessonProgress.setCompleted(true);
        progressRepository.save(lessonProgress);

        updateProgress(enrollmentId);
    }

    private void updateProgress(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
        int completed = progressRepository.countByEnrollmentId(enrollmentId);

        int total = lessonRepository.findAll().size();

        double percent = (completed * 100.0) / total;

        enrollment.setProgress(percent);

        if (percent == 100.0) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
            certificateService.generate(enrollment.getUserId(), enrollment.getCourseId());
        }

        enrollmentRepository.save(enrollment);
    }*/
}
