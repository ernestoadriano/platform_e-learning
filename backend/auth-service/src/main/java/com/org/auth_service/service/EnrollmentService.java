package com.org.auth_service.service;

import com.org.auth_service.dto.EnrollmentDTO;
import com.org.auth_service.model.Course;
import com.org.auth_service.model.Enrollment;
import com.org.auth_service.model.User;
import com.org.auth_service.model.enums.Role;
import com.org.auth_service.repository.CourseRepository;
import com.org.auth_service.repository.EnrollmentRepository;
import com.org.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<?> create(EnrollmentDTO dto) {
        Course course = courseRepository.findById(dto.idCourse())
                .orElseThrow(() -> new RuntimeException("Not found this course"));

        User user = userRepository.findById(dto.idStudent())
                .orElseThrow(() -> new RuntimeException("Not found this student"));

        if (user.getRole() != Role.STUDENT) {
            return ResponseEntity.badRequest().body(new RuntimeException("Not found this student"));
        }

        Enrollment enrollment = new Enrollment();

        enrollment.setCourse(course);
        enrollment.setUser(user);
        enrollment.setProgress(0.0);

        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok(toDTO(enrollment));
    }

    public EnrollmentDTO getById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this enrollment"));
        return toDTO(enrollment);
    }

    public EnrollmentDTO updateProgress(Long id, Double plusProgress) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this enrollment"));
        enrollment.setProgress(enrollment.getProgress() + plusProgress);
        enrollmentRepository.save(enrollment);
        return toDTO(enrollment);
    }


    private EnrollmentDTO toDTO(Enrollment enrollment) {
        return new EnrollmentDTO(enrollment.getUser().getId(),
                enrollment.getCourse().getId(),
                enrollment.getStatus(),
                enrollment.getProgress());
    }
}
