package com.org.assessment_service.repository;

import com.org.assessment_service.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    boolean existsByQuestionIdAndCorrectTrue(Long questionId);
}
