package com.ernesto.monolith.course.repository;

import com.ernesto.monolith.course.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    
    List<Module> findAllByCourseId(Long courseId);
    
    Optional<Module> findByCourseId(Long courseId);

    boolean existsByCourseId(Long courseId);
}
