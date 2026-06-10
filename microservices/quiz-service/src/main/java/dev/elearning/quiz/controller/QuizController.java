package dev.elearning.quiz.controller;

import dev.elearning.quiz.dto.request.QuizRequest;
import dev.elearning.quiz.dto.request.QuizSubmitRequest;
import dev.elearning.quiz.dto.response.QuizAttemptResponse;
import dev.elearning.quiz.dto.response.QuizResponse;
import dev.elearning.quiz.dto.response.QuizResultResponse;
import dev.elearning.quiz.dto.response.QuizWithQuestionsResponse;
import dev.elearning.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/lessons/{lessonId}")
    public ResponseEntity<QuizResponse> getQuizByLessonId(@PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(quizService.getQuizLessonId(lessonId));
    }

    @GetMapping("/lessons/{lessonId}/with-questions")
    public ResponseEntity<QuizWithQuestionsResponse> getQuizWithQuestions(@PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(quizService.getQuizWithQuestion(lessonId));
    }

    @GetMapping("/users/{usersId}/lessons/{lessonId}/passed")
    public ResponseEntity<Boolean> hasUserPassedQuiz(@PathVariable("userId") Long userId,
                                                     @PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(quizService.hasUserPassedQuiz(userId, lessonId));
    }

    @GetMapping("/users/{userId}/lessons/{lessonId}/remaining-attempts")
    public ResponseEntity<Integer> getRemainingAttempts(@PathVariable("userId") Long userId,
                                                        @PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(quizService.getRemainingAttempts(userId, lessonId));
    }

    @GetMapping("/users/{userId}/lessons/{lessonId}/attemps")
    public ResponseEntity<List<QuizAttemptResponse>> getUserAttempts(@PathVariable("userId") Long userId,
                                                                     @PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(quizService.getUserAttempts(userId, lessonId));
    }

    @PostMapping("/lessons/{lessonId}/submit")
    public ResponseEntity<QuizResultResponse> submitQuiz(@RequestHeader("userId") Long userId, @PathVariable("lessonId") Long lessonId, @RequestBody QuizSubmitRequest request) {
        return ResponseEntity.ok(quizService.submitQuiz(userId, lessonId, request));
    }

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@RequestBody QuizRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.createQuiz(request));
    }
}
