package com.org.course_service.controller;

import com.org.course_service.dto.CreateLessonDTO;
import com.org.course_service.dto.LessonDTO;
import com.org.course_service.dto.UpdateLessonDTO;
import com.org.course_service.model.Lesson;
import com.org.course_service.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService service;

    @GetMapping("/{moduleId}")
    public List<LessonDTO> getAllByModule(Long moduleId) {
        List<Lesson> lessons = service.getAllByModuleId(moduleId);
        List<LessonDTO> lessonDTOS = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonDTOS.add(toDTO(lesson));
        }

        return lessonDTOS;
    }

    @GetMapping("/{id}")
    public LessonDTO getById(@PathVariable("id") Long id) {
        return toDTO(service.getById(id));
    }

    @PostMapping
    public LessonDTO create(@RequestBody CreateLessonDTO dto) {
        return toDTO(service.create(dto));
    }

    @PutMapping("/{id}")
    public LessonDTO update(@PathVariable("id") Long id, @RequestBody UpdateLessonDTO dto) {
        return toDTO(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    private LessonDTO toDTO(Lesson lesson) {
        return new LessonDTO(lesson.getId(), lesson.getTitle(), lesson.getVideoUrl(), lesson.getOrderIndex());
    }
}
