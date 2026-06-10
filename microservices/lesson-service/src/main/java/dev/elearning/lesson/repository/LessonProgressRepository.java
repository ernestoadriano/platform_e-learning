package dev.elearning.lesson.repository;

import dev.elearning.lesson.model.LessonProgress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {

    Optional<LessonProgress> findByUserIdAndLessonId(Long userId, Long lessonId);

    List<LessonProgress> findAllByUserIdAndIsCompletedTrue(Long userId);

    @Query("SELECT lp FROM LessonProgress lp WHERE lp.userId = :userId AND lp.lessonId IN " +
            "(SELECT l.id FROM Lesson l WHERE l.moduleId = :moduleId)")
    List<LessonProgress> findAllByUserIdAndModuleId(@Param("userId") Long userId, @Param("moduleId") Long moduleId);

    Boolean existsByUserIdAndLessonIdAndIsCompletedTrue(Long userId, Long lessonId);

    @Query("SELECT COUNT(lp) FROM LessonProgress lp WHERE lp.userId = :userId AND " +
            "lp.isCompleted = true AND lp.lessonId IN (SELECT l.id FROM Lesson l WHERE l.moduleId = :moduleId)")
    Long countCompletedLessonsInModule(@Param("userId") Long userId, @Param("moduleId") Long moduleId);

    Long countCompletedLessons(@Param("courseId"))

    @Modifying
    @Transactional
    @Query("UPDATE LessonProgress lp SET lp.isCompleted = true, lp.score = :score, lp.completedAt = CURRENT_TIMESTAMP " +
            "WHERE lp.userId = :userId AND lp.lessonId = :lessonId")
    void markLessonAsCompleted(@Param("userId") Long userId, @Param("lessonId") Long lessonId, @Param("score") Integer score);
}
