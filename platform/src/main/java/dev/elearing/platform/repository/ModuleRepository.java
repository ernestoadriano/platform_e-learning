package dev.elearing.platform.repository;

import dev.elearing.platform.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {


    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId AND m.order = :moduleOrder")
    Optional<Module> findByCourseIdAndModuleOrder(@Param("courseId") Long courseId, @Param("moduleOrder") Integer moduleOrder);

    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId ORDER BY m.order ASC")
    List<Module> findAllByCourseIdOrderByModuleOrderAsc(@Param("courseId") Long courseId);
}