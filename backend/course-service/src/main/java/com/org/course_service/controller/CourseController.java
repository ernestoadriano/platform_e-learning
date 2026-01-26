package com.org.course_service.controller;

import com.org.course_service.dto.CourseDTO;
import com.org.course_service.model.Course;
import com.org.course_service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @GetMapping
    public List<CourseDTO> getAll() {
        List<Course> courses = service.getAll();
        List<CourseDTO> courseDTOS = new ArrayList<>();
        for (Course course : courses) {
            courseDTOS.add(toDTO(course));
        }

        return courseDTOS;
    }

    @GetMapping("/{id}")
    public CourseDTO getById(@PathVariable("id") Long id) {
        return toDTO(service.getById(id));
    }

    @PostMapping
    public CourseDTO create(@RequestBody CourseDTO dto) {
        return toDTO(service.create(dto));
    }

    @PutMapping("/{id}")
    public CourseDTO update(@PathVariable("id") Long id, @RequestBody CourseDTO dto) {
        return toDTO(service.update(id, dto));
    }

    @PutMapping("/publish/{id}")
    public CourseDTO publishCourse(@PathVariable("id") Long id) {
        return toDTO(service.publishCourse(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    private CourseDTO toDTO(Course course) {
        if (course.getId() == null) {
            return new CourseDTO(null, course.getTitle(), course.getDescription(), course.getPrice());
        }
        return new CourseDTO(course.getId(), course.getTitle(), course.getDescription(), course.getPrice());
    }
}
