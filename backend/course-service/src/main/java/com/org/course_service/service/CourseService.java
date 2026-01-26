package com.org.course_service.service;

import com.org.course_service.dto.CourseDTO;
import com.org.course_service.model.Course;
import com.org.course_service.repository.CourseRepository;
import com.org.course_service.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public Course create(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());

        return courseRepository.save(course);
    }

    public Course publishCourse(Long courseId) {
        Course course = getById(courseId);

        if (course.isPublished()) {
            throw new RuntimeException("Course already published");
        }

        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new RuntimeException("Course must have a valid price");
        }

        if (!moduleRepository.existsByCourseId(courseId)) {
            throw new RuntimeException("Course must have least one module");
        }

        course.setPublished(true);
        return courseRepository.save(course);
    }

    public List<Course> getAll() {
        Sort sort = Sort.by("title");
        return courseRepository.findAll(sort);
    }

    public List<Course> getAllCoursesPublished() {
        return courseRepository.findAllByPublishedTrueOrderByTitle();
    }

    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course update(Long id, CourseDTO dto) {
        Course course = getById(id);

        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            if (course.getPrice() <= 0) {
                throw new RuntimeException("The price must");
            }
            course.setPrice(dto.getPrice());
        }

        return courseRepository.save(course);
    }

    public void delete(Long id) {
        Course course = getById(id);
        courseRepository.delete(course);
    }
}
