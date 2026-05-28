package dev.elearing.platform.repository;

import dev.elearing.platform.model.ExamOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamOptionRepository extends JpaRepository<ExamOption, Long> {

    List<ExamOption> findByQuestionIdOrderByOptionOrderAsc(Long questionId);
}
