package dev.elearing.platform.repository;

import dev.elearing.platform.model.Course;
import dev.elearing.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Busca cursos que um usuário comprou
    @Query("SELECT c FROM Course c JOIN Purchase p ON p.course.id = c.id WHERE p.user.id = :userId")
    List<Course> findCoursesByUserId(@Param("userId") Long userId);

    // Busca cursos que um usuário ensina (como teacher)
    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.id = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") Long teacherId);
}