package dev.elearning.quiz.repository;

import dev.elearning.quiz.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    List<QuizAttempt> findAllByUserIdAndQuizOrderByAttemptNumberDesc(Long userId, Long quizId);
    Optional<QuizAttempt> findTopByUserIdAndQuizIdOrderByAttemptNumberDesc(Long userId, Long quizId);
    long countByUserIdAndQuizId(Long userId, Long quizId);
    boolean existsByUserIdAndQuizIdAndPassedTrue(Long userId, Long quizId);

    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.userId = :userId AND qa.quiz.lessonId = :lessonId ORDER BY qa.attemptNumber DESC")
    List<QuizAttempt> findAllByUserIdAndLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);
}
