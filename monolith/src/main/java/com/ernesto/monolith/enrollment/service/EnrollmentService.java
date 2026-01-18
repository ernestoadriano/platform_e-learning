package com.ernesto.monolith.enrollment.service;

import com.ernesto.monolith.assessment.service.ExamService;
import com.ernesto.monolith.certificate.service.CertificateService;
import com.ernesto.monolith.common.dto.NotificationType;
import com.ernesto.monolith.course.repository.LessonRepository;
import com.ernesto.monolith.enrollment.model.Enrollment;
import com.ernesto.monolith.enrollment.model.LessonProgress;
import com.ernesto.monolith.enrollment.model.enums.EnrollmentStatus;
import com.ernesto.monolith.enrollment.repository.EnrollmentRepository;
import com.ernesto.monolith.enrollment.repository.LessonProgressRepository;
import com.ernesto.monolith.notification.service.NotificationService;
import com.ernesto.monolith.order.model.Purchase;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonRepository lessonRepository;


    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private NotificationService notificationService;

    public void createEnrollment(Purchase purchase) {
        boolean alreadyEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(purchase.getStudentId(), purchase.getCourseId());

        if (alreadyEnrolled) {
            return;
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setStudentId(purchase.getStudentId());

        enrollment.setCourseId(purchase.getCourseId());

        enrollmentRepository.save(enrollment);

        notificationService.notify(purchase.getStudentId(),
                "Enrollment is complete",
                "You have have access of course.",
                NotificationType.ENROLLMENT);
    }

    public void markCourseCompleted(Enrollment enrollment, User student) {
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepository.save(enrollment);
        notificationService.notify(student.getId(),
                "Course complete",
                "Congratulations! You complete the course with success.",
                NotificationType.COURSE);
        certificateService.generate(student.getId(), enrollment.getCourseId());
    }

    public void markModuleCompleted(Enrollment enrollment, Long moduleId) {
        enrollment.getCompletedModules().add(moduleId);
        enrollmentRepository.save(enrollment);
    }

    public void markLessonCompleted(Long enrollmentId, Long lessonId, User student) {

        if (progressRepository.existsByEnrollmentIdAndLessonId(enrollmentId, lessonId)) {
            return;
        }
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow();

        if (!Objects.equals(student.getId(), enrollment.getStudentId())) {
            throw new RuntimeException("Forbidden");
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

        enrollmentRepository.save(enrollment);
    }
}
