package dev.elearning.course.repository;

import dev.elearning.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByCategoryId(Long categoryId);

    List<Course> findAllByPublishedTrue();

    List<Course> findAllByInstructorId(Long instructorId);

    List<Course> findAllByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Course> findAllTitleContainingIgnoreCase(String title);

    Page<Course> findAllByPublishedTrue(Pageable pageable);

    Long countByInstructorId(Long instructorId);

    @Query("SELECT c FROM Course c WHERE c.published = true ORDER BY c.publishedAt DESC")
    List<Course> findRecentCourses(Pageable pageable);

    @Query("SELECT c FROM Course c ORDER BY c.enrollmentStudents DESC")
    List<Course> findMostPopularCourses(Pageable pageable);
}
