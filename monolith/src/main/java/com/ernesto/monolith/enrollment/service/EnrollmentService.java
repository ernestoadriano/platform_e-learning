package com.ernesto.monolith.enrollment.service;

import com.ernesto.monolith.certificate.service.CertificateService;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Lesson;
import com.ernesto.monolith.course.model.Module;
import com.ernesto.monolith.course.repository.LessonRepository;
import com.ernesto.monolith.course.repository.ModuleRepository;
import com.ernesto.monolith.enrollment.model.Enrollment;
import com.ernesto.monolith.enrollment.model.LessonProgress;
import com.ernesto.monolith.enrollment.model.enums.EnrollmentStatus;
import com.ernesto.monolith.enrollment.repository.EnrollmentRepository;
import com.ernesto.monolith.enrollment.repository.LessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {


    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private CertificateService certificateService;

    public Enrollment getActiveEnrollment(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new BusinessException("Student not found"));
        if (!enrollment.getStatus().equals(EnrollmentStatus.ACTIVE)) {
            throw new BusinessException("This course is already complete");
        }

        return enrollment;
    }

    public boolean isNextLesson(Enrollment enrollment, Lesson lesson) {
        List<Lesson> lessons = lessonRepository.findByModuleIdOrderByOrderIndex(lesson.getModuleId());
        for (Lesson l : lessons) {
            if (!enrollment.getCompletedLessons().contains(l.getId())) {
                return l.getId().equals(lesson.getId());
            }
        }
        return false;
    }

    public void markLessonCompleted(Enrollment enrollment, Lesson lesson) {
        enrollment.getCompletedLessons().add(lesson.getId());
        enrollmentRepository.save(enrollment);
    }

    public boolean isLastLessonOfModule(Enrollment enrollment, Lesson lesson) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(lesson.getModuleId());
        return enrollment.getCompletedLessons().containsAll(lessons.stream().map(Lesson::getId).toList());
    }


    public boolean allLessonsCompleted(Enrollment enrollment, Long moduleId) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);

        return enrollment.getCompletedLessons().containsAll(lessons.stream().map(Lesson::getId).toList());
    }

    public void markModuleCompleted(Enrollment enrollment, Long moduleId) {
        enrollment.getCompletedModules().add(moduleId);
        enrollmentRepository.save(enrollment);
    }

    public boolean isLastModuleOfCourse(Module module, Enrollment enrollment) {
        List<Module> modules = moduleRepository.findAllByCourseId(module.getCourseId());
        return modules.stream()
                .map(Module::getId)
                .allMatch(id ->
                        enrollment.getCompletedModules().contains(id));
    }

    public boolean allModulesCompleted(Enrollment enrollment) {
        List<Module> modules = moduleRepository.findAllByCourseId(enrollment.getCourseId());
        return enrollment.getCompletedModules().containsAll(modules.stream().map(Module::getId).toList());
    }

    public void markCourseCompleted(Enrollment enrollment) {
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        enrollmentRepository.save(enrollment);
    }

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
    }
}
