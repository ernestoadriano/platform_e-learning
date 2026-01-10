package com.org.auth_service.service;

import com.org.auth_service.dto.CourseDTO;
import com.org.auth_service.dto.LessonDTO;
import com.org.auth_service.model.Course;
import com.org.auth_service.model.Instructor;
import com.org.auth_service.model.Lesson;
import com.org.auth_service.repository.CourseRepository;
import com.org.auth_service.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    public List<CourseDTO> getAll() {
        List<Course> courses = courseRepository.findAll();
        List<CourseDTO> responses = new ArrayList<>();
        for (Course course : courses) {
            responses.add(toDTO(course));
        }

        return responses;
    }

    public CourseDTO getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this course"));
        return toDTO(course);
    }

    public CourseDTO insert(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.title());
        course.setDescription(dto.description());
        course.setPrice(dto.price());
        Instructor instructor = instructorRepository.findById(dto.idInstructor())
                        .orElseThrow(() -> new RuntimeException("Not found this instructor"));
        course.setInstructor(instructor);
        courseRepository.save(course);
        return toDTO(course);
    }

    public CourseDTO update(Long id, CourseDTO request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this course"));

        if (request.title() != null) {
            course.setTitle(request.title());
        }

        if (request.description() != null) {
            course.setDescription(request.description());
        }

        if (request.price() != null) {
            course.setPrice(request.price());
        }

        if (request.idInstructor() != null) {
            Instructor instructor = instructorRepository.findById(request.idInstructor())
                    .orElseThrow(() -> new RuntimeException("Not found this instructor"));
            course.setInstructor(instructor);
        }

        courseRepository.save(course);

        return toDTO(course);
    }

    public List<LessonDTO> getLessonsById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this course"));

        List<Lesson> lessons = course.getLessons();
        List<LessonDTO> lessonsDTO = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonsDTO.add(new LessonDTO(lesson.getTitle(), lesson.getVideoUrl(), lesson.getDuration(), null));
        }

        return lessonsDTO;
    }

    public List<CourseDTO> delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this course"));
        courseRepository.delete(course);
        return getAll();
    }

    private CourseDTO toDTO(Course course) {
        return new CourseDTO(course.getTitle(), course.getDescription(),
                course.getPrice(), course.getInstructor().getName(),null);
    }
}
