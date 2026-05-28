package dev.elearing.platform.repository;

import dev.elearing.platform.model.ExamAnswer;
import dev.elearing.platform.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {

    List<ExamAnswer> findByAttempt(ExamAttempt attempt);

    List<ExamAnswer> findByAttemptId(Long attemptId);

    long countByAttemptIdAndIsCorrectTrue(Long attemptId);

}
