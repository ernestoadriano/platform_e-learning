package dev.elearing.platform.repository;

import dev.elearing.platform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l WHERE l.module.id = :moduleId ORDER BY l.order ASC")
    List<Lesson> findAllByModuleIdOrderByLessonOrderAsc(@Param("moduleId") Long moduleId);

    @Query("SELECT l FROM Lesson l WHERE l.module.course.id = :courseId ORDER BY l.module.order ASC, l.order ASC")
    List<Lesson> findAllByCourseIdOrdered(@Param("courseId") Long courseId);
}