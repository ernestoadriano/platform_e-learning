package com.org.assessment_service.repository;

import com.org.assessment_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByExamId(Long examId);

    boolean existsByExamId(Long examId);
}
