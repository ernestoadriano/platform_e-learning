package dev.elearning.quiz.repository;

import dev.elearning.quiz.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByLessonId(Long lessonId);
    List<Quiz> findAllByLessonIdIn(List<Long> lessonIds);
    Boolean existsByLessonId(Long lessonId);
}
