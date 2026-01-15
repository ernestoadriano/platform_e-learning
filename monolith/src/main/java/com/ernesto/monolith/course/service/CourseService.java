package com.ernesto.monolith.course.service;

import com.ernesto.monolith.common.dto.CourseDTO;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Course;
import com.ernesto.monolith.course.repository.CourseRepository;
import com.ernesto.monolith.course.repository.ModuleRepository;
import com.ernesto.monolith.enrollment.model.Enrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public CourseDTO create(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());

        course = courseRepository.save(course);
        return toDTO(course);
    }

    public CourseDTO publishCourse(Long courseId) {
        Course course = getCourseById(courseId);
        if (course.getPublished()) {
            throw new BusinessException("Course already published");
        }

        if (course.getPrice() == null || course.getPrice() <= 0) {
            throw new BusinessException("Course must have a valid price");
        }

        if (!moduleRepository.existsByCourseId(courseId)) {
            throw new RuntimeException("Course must have at least one module");
        }

        course.setPublished(true);
        course = courseRepository.save(course);

        return toDTO(course);
    }

    public List<CourseDTO> getAll() {
        Sort sort = Sort.by("createdAt");
        List<Course> courses = courseRepository.findAll(sort);
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courses) {
            if (course.getPublished())
                courseDTOS.add(toDTO(course));
        }
        return courseDTOS;
    }

    public CourseDTO getById(Long id) {
        Course course = getCourseById(id);
        return toDTO(course);
    }

    public CourseDTO update(Long id, CourseDTO dto) {
        Course course = getCourseById(id);

        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            course.setPrice(dto.getPrice());
        }

        course = courseRepository.save(course);

        return toDTO(course);
    }

    public void delete(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    private Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    private CourseDTO toDTO(Course course) {
        if (course.getId() == null) {
            return new CourseDTO(null, course.getTitle(), course.getDescription(), course.getPrice());
        }
        return new CourseDTO(course.getId(), course.getTitle(), course.getDescription(), course.getPrice());
    }
}
