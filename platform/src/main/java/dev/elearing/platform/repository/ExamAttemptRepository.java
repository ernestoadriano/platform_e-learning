package dev.elearing.platform.repository;

import dev.elearing.platform.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    List<ExamAttempt> findByUserIdAndModuleIdOrderByCreatedAtDesc(Long userId, Long moduleId);

    Boolean existsByUserIdAndModuleIdAndPassed(Long userId, Long moduleId, Boolean passed);

    List<ExamAttempt> findByUserId(Long userId);
}
