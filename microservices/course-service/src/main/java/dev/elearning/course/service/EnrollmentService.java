package dev.elearning.course.service;

import dev.elearning.course.dto.request.EnrollmentRequest;
import dev.elearning.course.dto.response.EnrollmentResponse;
import dev.elearning.course.model.Course;
import dev.elearning.course.model.Enrollment;
import dev.elearning.course.repository.CourseRepository;
import dev.elearning.course.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Transactional
    public EnrollmentResponse enrollUser(EnrollmentRequest request) {

        if (enrollmentRepository.existsByUserIdAndCourseIdAndActiveTrue(request.getUserId(), request.getCourseId())) {
            throw new RuntimeException("User already enrolled in this course");
        }

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found: " + request.getCourseId()));

        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(request.getUserId());
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());

        if (request.getExpiredInDays() != null) {
            enrollment.setExpiresAt(LocalDateTime.now().plusDays(request.getExpiredInDays()));
        }

        courseService.incrementEnrolledStudents(request.getCourseId());

        enrollment = enrollmentRepository.save(enrollment);

        return toResponse(enrollment);
    }

    public List<EnrollmentResponse> getUserEnrollments(Long userId) {
        return enrollmentRepository.findAllUserIdAndActiveTrue(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findAllCourseIdAndActiveTrue(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrollmentRepository.existsByUserIdAndCourseIdAndActiveTrue(userId, courseId);
    }

    @Transactional
    public void cancelEnrollment(Long userId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setActive(false);
        enrollmentRepository.save(enrollment);

        courseService.decrementEnrolledStudents(courseId);
    }

    public Long countEnrolledStudents(Long courseId) {
        return enrollmentRepository.countByCourseIdAndActiveTrue(courseId);
    }

    @Transactional
    public void processExpiredEnrollments() {
        List<Enrollment> expired = enrollmentRepository.findExpiredActiveEnrollments();

        for (Enrollment enrollment : expired) {
            enrollment.setActive(false);
            enrollmentRepository.save(enrollment);
            courseService.decrementEnrolledStudents(enrollment.getCourse().getId());
        }
    }

    private EnrollmentResponse toResponse(Enrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();
        response.setId(enrollment.getId());
        response.setUserId(enrollment.getUserId());
        response.setCourseId(enrollment.getCourse().getId());
        response.setEnrolledAt(enrollment.getEnrolledAt());
        response.setExpiresAt(enrollment.getExpiresAt());
        response.setActive(enrollment.getActive());

        courseRepository.findById(enrollment.getId()).ifPresent(course -> {
            response.setCourseTitle(course.getTitle());
            response.setCourseImageUrl(course.getImageUrl());
        });

        return response;
    }
}
