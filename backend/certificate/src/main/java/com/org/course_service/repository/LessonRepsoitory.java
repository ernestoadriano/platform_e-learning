package com.org.course_service.repository;

import com.org.course_service.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepsoitory extends JpaRepository<Lesson, Long> {

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    boolean existsByVideoUrl(String videoUrl);

    boolean existsByVideoUrlAndIdNot(String videoUrl, Long id);

    List<Lesson> findAllByModuleId(Long moduleId);
}
