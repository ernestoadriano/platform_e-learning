package dev.elearing.platform.repository;

import dev.elearing.platform.model.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    boolean existsByUserIdAndLessonIdAndCompleted(Long userId, Long lessonId, Boolean completed);

    List<UserProgress> findAllByUserIdAndCourseId(Long userId, Long courseId);

    //Integer countCoursesInProgress(Long userId);

    Integer countCompletedLessonsByUserId(Long userId);

    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);


}
