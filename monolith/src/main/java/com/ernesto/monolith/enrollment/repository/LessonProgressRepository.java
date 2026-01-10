package com.ernesto.monolith.enrollment.repository;

import com.ernesto.monolith.enrollment.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    int countByEnrollmentId(Long enrollmentId);

    Boolean existsByEnrollmentIdAndLessonId(Long enrollmentId, Long lessonId);
}
