package com.ernesto.monolith.assessment.repository;

import com.ernesto.monolith.assessment.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByExamId(Long examId);
}
