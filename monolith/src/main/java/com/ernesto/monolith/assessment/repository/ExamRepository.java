package com.ernesto.monolith.assessment.repository;

import com.ernesto.monolith.assessment.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    Optional<Exam> findByCourseId(Long courseId);
}
