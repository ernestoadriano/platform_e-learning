package dev.elearing.platform.repository;

import dev.elearing.platform.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    boolean existsByUserIdAndLessonIdAndCompleted(Long userId, Long lessonId, Boolean completed);

    // CORRIGIDO: a consulta está correta
    @Query("SELECT up FROM UserProgress up WHERE up.user.id = :userId AND up.lesson.module.course.id = :courseId")
    List<UserProgress> findAllByUserIdAndCourseId(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT COUNT(up) FROM UserProgress up WHERE up.user.id = :userId AND up.completed = true")
    Integer countCompletedLessonsByUserId(@Param("userId") Long userId);

    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
}