package dev.elearning.progress.repository;

import dev.elearning.progress.model.ModuleProgress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Long> {

    Optional<ModuleProgress> findByUserIdAndModuleId(Long userId, Long moduleId);

    List<ModuleProgress> findByUserId(Long userId);

    List<ModuleProgress> findByUserIdAndIsCompletedTrue(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ModuleProgress mp SET mp.completedLessons = :completedLessons, " +
            "mp.percentage = :percentage,mp.isCompleted = :isCompleted, " +
            "mp.completedAt = CURRENT_TIMESTAMP WHERE mp.userId = :userId " +
            "AND mp.moduleId = :moduleId")
    void updateModuleProgress(@Param("userId") Long userId, @Param("moduleId") Long moduleId,
                              @Param("completedLessons") Integer completedLessons,
                              @Param("percentage") Double percentage,
                              @Param("isCompleted") Boolean isCompleted);


    @Modifying
    @Transactional
    @Query("UPDATE ModuleProgress mp SET mp.examPassed = :passed, mp.examScore = :score " +
            "WHERE mp.userId = :userId AND mp.moduleId = :moduleId")
    void updateExamResult(@Param("userId") Long userId, @Param("moduleId") Long moduleId,
                          @Param("passed") Boolean passed, @Param("score") Double score);
}
