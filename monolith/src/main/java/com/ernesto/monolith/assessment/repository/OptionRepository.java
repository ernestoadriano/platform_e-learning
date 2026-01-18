package com.ernesto.monolith.assessment.repository;

import com.ernesto.monolith.assessment.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByQuestionIdAndCorrectTrue(Long questionId);
}
