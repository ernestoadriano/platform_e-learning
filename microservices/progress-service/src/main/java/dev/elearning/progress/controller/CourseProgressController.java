package dev.elearning.progress.controller;

import dev.elearning.progress.dto.response.CourseProgressResponse;
import dev.elearning.progress.service.ProgressCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress/courses")
public class CourseProgressController {

    @Autowired
    private ProgressCalculationService progressService;

    @GetMapping("/users/{userId}/courses/{coursesId}")
    public ResponseEntity<CourseProgressResponse> getCourseProgress(@PathVariable("userId") Long userId,
                                                                    @PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(progressService.calculateCourseProgress(userId, courseId));
    }

    @PostMapping("/users/{userId}/lessons/{lessonId}/complete")
    public ResponseEntity<Void> updateLessonProgress(@PathVariable("userId") Long userId,
                                                     @RequestParam("moduleId") Long moduleId,
                                                     @RequestParam("courseId") Long courseId,
                                                     @RequestParam(value = "quizScore", required = false)
                                                         Double quizScore) {
        progressService.updateAfterLessonCompletion(userId, moduleId, courseId, quizScore);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/modules/{moduleId}/exam-result")
    public ResponseEntity<Void> updateModuleExamResult(@PathVariable("userId") Long userId,
                                                       @PathVariable("moduleId") Long moduleId,
                                                       @RequestParam("passed") Boolean passed,
                                                       @RequestParam("score") Double score) {
        progressService.updateModuleExamResult(userId, moduleId, passed, score);
        return ResponseEntity.ok().build();
    }


}
