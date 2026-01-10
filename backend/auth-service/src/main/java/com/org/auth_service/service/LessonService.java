package com.org.auth_service.service;

import com.org.auth_service.dto.LessonDTO;
import com.org.auth_service.model.Course;
import com.org.auth_service.model.Lesson;
import com.org.auth_service.repository.CourseRepository;
import com.org.auth_service.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    public LessonDTO create(LessonDTO dto) {
        Course course = courseRepository.findById(dto.idCourse())
                .orElseThrow(() -> new RuntimeException("Not found this course"));
        Lesson lesson = new Lesson(null, dto.title(), dto.videoUrl(), dto.duration(), course);
        lessonRepository.save(lesson);

        return dto;
    }

    public LessonDTO update(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this lesson"));

        if (dto.title() != null) {
            lesson.setTitle(dto.title());
        }

        if (dto.videoUrl() != null) {
            lesson.setVideoUrl(dto.videoUrl());
        }

        if (dto.idCourse() != null) {
            Course course = courseRepository.findById(dto.idCourse())
                    .orElseThrow(() -> new RuntimeException("Not found this course"));
            lesson.setCourse(course);
        }
        lessonRepository.save(lesson);

        return dto;
    }

    public ResponseEntity<?> delete(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this lesson"));
        lessonRepository.delete(lesson);
        return ResponseEntity.ok("");
    }
}
