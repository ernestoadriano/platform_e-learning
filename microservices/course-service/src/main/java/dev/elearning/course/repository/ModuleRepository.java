package dev.elearning.course.repository;

import dev.elearning.course.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    List<Module> findAllByCourseIdOrderByModuleOrderAsc(Long courseId);

    List<Module> findAllByCourseId(Long courseId);

    Long countByCourseId(Long courseId);

    Boolean existsByCourseIdAndId(Long courseId, Long moduleId);

    Module findByCourseIdAndModuleOrder(Long courseId, Integer moduleOrder);
}
