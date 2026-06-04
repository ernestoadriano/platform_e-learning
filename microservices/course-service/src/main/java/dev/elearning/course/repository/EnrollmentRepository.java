package dev.elearning.course.repository;

import dev.elearning.course.model.Enrollment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);

    boolean existsByUserIdAndCourseIdAndActiveTrue(Long userId, Long courseId);

    List<Enrollment> findAllUserIdAndActiveTrue(Long userId);

    List<Enrollment> findAllCourseIdAndActiveTrue(Long courseId);

    Long countByCourseIdAndActiveTrue(Long courseId);

    @Modifying
    @Transactional
    @Query("UPDATE Enrollment e SET e.active = false WHERE e.userId = :userId AND e.course.id = :courseId")
    void deactivateEnrollment(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT e FROM Enrollment e WHERE e.active = true AND e.expiresAt < CURRENT_TIMESTAMP")
    List<Enrollment> findExpiredActiveEnrollments();
}
