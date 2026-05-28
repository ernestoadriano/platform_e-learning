package dev.elearing.platform.repository;

import dev.elearing.platform.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findByExamIdOrderById(Long examId);

    List<ExamQuestion> findByExamModuleId(Long moduleId);
}
