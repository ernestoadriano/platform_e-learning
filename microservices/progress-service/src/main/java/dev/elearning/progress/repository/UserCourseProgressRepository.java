package dev.elearning.progress.repository;

import dev.elearning.progress.model.UserCourseProgress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {

    Optional<UserCourseProgress> findByUserIdAndCourseId(Long userId, Long courseId);

    List<UserCourseProgress> findByUserId(Long userId);

    List<UserCourseProgress> findByUserIdAndIsCompletedTrue(Long userId);

    Long countByUserIdAndIsCompletedTrue(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserCourseProgress ucp SET ucp.completedLesson = :completedLessons, " +
            "ucp.percentage = :percentage, ucp.isCompleted = :isCompleted, " +
            "ucp.completedAt = CURRENT_TIMESTAMP WHERE ucp.userId = :userId AND " +
            "ucp.courseId = :courseId")
    void updateProgress(@Param("userId") Long userId, @Param("courseId") Long courseId,
                        @Param("completedLessons") Integer completedLessons, @Param("percentage") Double percentage,
                        @Param("isCompleted") Boolean isCompleted);
}
