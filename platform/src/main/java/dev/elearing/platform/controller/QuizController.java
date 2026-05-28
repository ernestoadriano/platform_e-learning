package dev.elearing.platform.controller;

import dev.elearing.platform.dto.LessonDTO;
import dev.elearing.platform.dto.QuestionDTO;
import dev.elearing.platform.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<List<QuestionDTO>> getQuizQuestions(@PathVariable Long lessonId) {
        LessonDTO lesson = lessonService.getById(lessonId);
        return ResponseEntity.ok(lesson.getQuestions());
    }
}