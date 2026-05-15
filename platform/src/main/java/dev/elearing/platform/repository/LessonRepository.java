package dev.elearing.platform.repository;

import dev.elearing.platform.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByModuleIdOrderByOrderAsc(Long moduleId);

    List<Lesson> findAllByCourseId(Long courseId);

}
