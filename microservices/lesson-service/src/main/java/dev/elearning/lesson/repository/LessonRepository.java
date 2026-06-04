package dev.elearning.lesson.repository;

import dev.elearning.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByModuleIdOrderByLessonOrderAsc(Long moduleId);

    List<Lesson> findAllByModuleId(Long moduleId);

    Optional<Lesson> findByModuleIdAndLessonOrder(Long moduleId, Integer lessonOrder);

    Optional<Lesson> findTopByModuleIdOrderByLessonAsc(Long moduleId);

    Optional<Lesson> findTopByModuleIdOrderByLessonDesc(Long moduleId);

    Long countByModuleId(Long moduleId);

    List<Lesson> findByIsFreeTrue();

    List<Lesson> findAllByModuleIdIn(List<Long> moduleIds);

    @Query("SELECT l FROM Lesson l WHERE l.moduleId = :moduleId ORDER BY l.lessonOrder ASC")
    List<Lesson> findAllByModuleIdWithPagination(@Param("moduleId") Long moduleId);
}
