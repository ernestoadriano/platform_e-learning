package dev.elearing.platform.controller;

import dev.elearing.platform.dto.LessonDTO;
import dev.elearing.platform.dto.OptionDTO;
import dev.elearing.platform.dto.QuestionDTO;
import dev.elearing.platform.dto.QuizAnswerRequest;
import dev.elearing.platform.model.User;
import dev.elearing.platform.service.LessonService;
import dev.elearing.platform.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/modules/{moduleId}/lessons")
@CrossOrigin(origins = "*")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<LessonDTO>> getLessonsByModule(@PathVariable Long moduleId) {
        return ResponseEntity.ok(lessonService.getAllByModule(moduleId));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Long lessonId) {
        return ResponseEntity.ok(lessonService.getById(lessonId));
    }

    @GetMapping("/{lessonId}/can-access")
    public ResponseEntity<Boolean> canAccessLesson(@PathVariable Long lessonId, @AuthenticationPrincipal User user) {
        boolean canAccess = progressService.canAccessLesson(user.getId(), lessonId);
        return ResponseEntity.ok(canAccess);
    }

    @GetMapping("/{lessonId}/completed")
    public ResponseEntity<Boolean> isLessonCompleted(@PathVariable Long lessonId, @AuthenticationPrincipal User user) {
        boolean completed = lessonService.isLessonCompleted(user.getId(), lessonId);
        return ResponseEntity.ok(completed);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LessonDTO> createLesson(@PathVariable Long moduleId, @RequestBody LessonDTO lessonDTO) {
        return ResponseEntity.ok(lessonService.createLesson(moduleId, lessonDTO));
    }

    @PutMapping("/{lessonId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Long lessonId, @RequestBody LessonDTO lessonDTO) {
        return ResponseEntity.ok(lessonService.update(lessonId, lessonDTO));
    }

    @DeleteMapping("/{lessonId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.ok().build();
    }

    // ENDPOINT SIMPLIFICADO - SEM usar o moduleId
    @PostMapping("/{lessonId}/complete")
    public ResponseEntity<?> completeLesson(
            @PathVariable Long moduleId,
            @PathVariable Long lessonId,
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> payload) {

        System.out.println("=== COMPLETE LESSON CALLED ===");
        System.out.println("Module ID: " + moduleId);
        System.out.println("Lesson ID: " + lessonId);
        System.out.println("User ID: " + user.getId());
        System.out.println("Payload: " + payload);

        try {
            // Extrair as respostas do payload
            @SuppressWarnings("unchecked")
            List<Integer> answers = (List<Integer>) payload.get("answers");

            // Buscar a lição
            LessonDTO lesson = lessonService.getById(lessonId);

            // Calcular o score
            int score = calculateScore(lesson, answers);

            // Salvar o progresso
            lessonService.completeLesson(lessonId, user.getId(), score);

            // Retornar resposta
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("score", score);
            response.put("message", score >= 70 ? "Lesson completed successfully!" : "Score too low. Need 70% to pass.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    private int calculateScore(LessonDTO lesson, List<Integer> answers) {
        List<QuestionDTO> questions = lesson.getQuestions();

        System.out.println("=== SCORE DETAIL ===");
        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            QuestionDTO q = questions.get(i);
            int userAnswer = i < answers.size() ? answers.get(i) : -1;
            boolean isCorrect = userAnswer == q.getCorrectAnswer();

            System.out.println((i+1) + ". " + q.getText());
            System.out.println("   Correct: " + q.getCorrectAnswer() + " | User: " + userAnswer + " | " + (isCorrect ? "✓" : "✗"));

            if (isCorrect) correct++;
        }

        int score = (correct * 100) / questions.size();
        System.out.println("Total: " + correct + "/" + questions.size() + " = " + score + "%");

        return score;
    }   
}