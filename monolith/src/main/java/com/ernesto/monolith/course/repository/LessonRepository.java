package com.ernesto.monolith.course.repository;

import com.ernesto.monolith.course.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByTitle(String title);

    boolean existsByVideoUrl(String videoUrl);

    List<Lesson> findAllByModuleId(Long moduleId);


    List<Lesson> findByModuleIdOrderByOrderIndex(Long moduleId);
}
