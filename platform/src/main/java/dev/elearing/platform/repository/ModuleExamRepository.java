package dev.elearing.platform.repository;

import dev.elearing.platform.model.ModuleExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleExamRepository extends JpaRepository<ModuleExam, Long> {

    Optional<ModuleExam> findByModuleId(Long moduleId);
}
