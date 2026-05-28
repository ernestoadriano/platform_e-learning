package dev.elearing.platform.controller;

import dev.elearing.platform.model.User;
import dev.elearing.platform.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    @Autowired
    private ProgressService progressService;

    @GetMapping("/courses/{courseId}/can-take")
    public ResponseEntity<Boolean> canTakeExam(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        var progress = progressService.getCourseProgress(user.getId(), courseId);
        boolean canTake = progress.getPercentage() == 100;
        return ResponseEntity.ok(canTake);
    }

    @PostMapping("/courses/{courseId}/submit")
    public ResponseEntity<?> submitExam(@PathVariable Long courseId, @AuthenticationPrincipal User user, @RequestBody ExamAnswersRequest request) {
        // Implementar lógica do exame final
        // Verificar se todas as aulas foram completadas
        // Calcular nota
        // Se passar, gerar certificado
        return ResponseEntity.ok().build();
    }
}

// DTO necessário
record ExamAnswersRequest(List<Integer> answers) {}