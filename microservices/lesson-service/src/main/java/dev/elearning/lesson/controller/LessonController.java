package dev.elearning.lesson.controller;

import dev.elearning.lesson.dto.request.LessonRequest;
import dev.elearning.lesson.dto.response.LessonResponse;
import dev.elearning.lesson.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<LessonResponse>> getLessonsByModule(@PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.getLessonsByModule(moduleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping("/module/{moduleId}/order/{lessonOrder}")
    public ResponseEntity<LessonResponse> getLessonByModuleAndOrder(@PathVariable("moduleId") Long moduleId, @PathVariable("lessonOrder") Integer lessonOrder) {
        return ResponseEntity.ok(lessonService.getLessonByModuleOrder(moduleId, lessonOrder));
    }

    @GetMapping("/module/{moduleId}/first")
    public ResponseEntity<LessonResponse> getFirstLesson(@PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.getFirstLesson(moduleId));
    }

    @GetMapping("/module/{moduleId}/last")
    public ResponseEntity<LessonResponse> getLastLesson(@PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.getLastLesson(moduleId));
    }

    @GetMapping("/module/{moduleId}/count")
    public ResponseEntity<Long> countLessonByModule(@PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.countLessonsByModule(moduleId));
    }

    @GetMapping("/free")
    public ResponseEntity<List<LessonResponse>> getFreeLessons() {
        return ResponseEntity.ok(lessonService.getFreeLessons());
    }

    @PostMapping
    public ResponseEntity<LessonResponse> create(@RequestBody LessonRequest request) {
        LessonResponse response = lessonService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<LessonResponse>> createLessons(@RequestBody List<LessonRequest> requests) {
        List<LessonResponse> responses = lessonService.createLessons(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> update(@PathVariable("id") Long id, @RequestBody LessonRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(id, request));
    }

    @PatchMapping("/module/{moduleId}/reorder")
    public ResponseEntity<Void> reorderLessons(@PathVariable("moduleId") Long moduleId, @RequestBody List<Long> lessonIds) {
        lessonService.reorderLesson(moduleId, lessonIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/module/{moduleId}")
    public ResponseEntity<Void> deleteByModule(@PathVariable("moduleId") Long moduleId) {
        lessonService.deleteByModule(moduleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lessonId}/belongs-to/{moduleId}")
    public ResponseEntity<Boolean> lessonBelongsToModule(@PathVariable("lessonId") Long lessonId, @PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(lessonService.lessonBelongsToModule(lessonId, moduleId));
    }
}
