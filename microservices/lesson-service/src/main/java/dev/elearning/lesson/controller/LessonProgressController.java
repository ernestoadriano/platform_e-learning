package dev.elearning.lesson.controller;

import dev.elearning.lesson.dto.request.LessonProgressRequest;
import dev.elearning.lesson.dto.response.LessonProgressResponse;
import dev.elearning.lesson.service.LessonProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-progress")
public class LessonProgressController {

    @Autowired
    private LessonProgressService progressService;

    @GetMapping("/users/{userId}/lessons/{lessonId}")
    public ResponseEntity<LessonProgressResponse> getLessonProgress(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId) {
        LessonProgressResponse response = progressService.getLessonProgress(userId, lessonId);
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/completed")
    public ResponseEntity<List<LessonProgressResponse>> getUserCompletedLesson(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(progressService.getUserCompletedLessons(userId));
    }

    @GetMapping("/users/{userId}/modules/{moduleId}")
    public ResponseEntity<List<LessonProgressResponse>> getModuleProgress(@PathVariable("userId") Long userId, @PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(progressService.getModuleProgress(userId, moduleId));
    }

    @GetMapping("/users/{userId}/modules/{moduleId}/completed-count")
    public ResponseEntity<Long> countCompletedLessonsInModule(@PathVariable("userId") Long userId, @PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(progressService.countCompletedLessonsInModule(userId, moduleId));
    }

    @GetMapping("/users/{userId}/lessons/{lessonId}/completed")
    public ResponseEntity<Boolean> isLessonCompleted(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(progressService.isLessonCompleted(userId, lessonId));
    }

    @PostMapping("/complete")
    public ResponseEntity<LessonProgressResponse> completeLesson(@RequestBody LessonProgressRequest request) {
        LessonProgressResponse response = progressService.completeLesson(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}/lessons/{lessonId}")
    public ResponseEntity<Void> resetLessonProgress(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId) {
        progressService.resetLessonProgress(userId, lessonId);
        return ResponseEntity.noContent().build();
    }
}

