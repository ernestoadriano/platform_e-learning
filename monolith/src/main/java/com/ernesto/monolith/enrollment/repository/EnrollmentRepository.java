package com.ernesto.monolith.enrollment.repository;

import com.ernesto.monolith.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByStudentIdAndCourseId(Long userId, Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}
