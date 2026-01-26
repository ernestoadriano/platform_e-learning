package com.org.assessment_service.repository;

import com.org.assessment_service.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    boolean existsByStudentIdAndCourseIdAndPassedTrue(Long studentId, Long courseId);

    boolean existsByStudentIdAndModuleIdAndPassedTrue(Long studentId, Long moduleId);

    boolean existsByStudentIdAndLessonIdAndPassedTrue(Long studentId, Long lessonId);
}
