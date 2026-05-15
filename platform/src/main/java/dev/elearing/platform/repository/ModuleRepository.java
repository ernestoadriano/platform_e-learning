package dev.elearing.platform.repository;

import dev.elearing.platform.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByCourseIdAndOrder(Long courseId, Integer order);
}
