package dev.elearning.progress.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quiz-service", url = "${quiz.service.url:http://localhost:8085}")
public interface QuizClient {

    @GetMapping("/api/quizzes/users/{userId}/lessons/{lessonId}/passed")
    Boolean hasUserPassedQuiz(@PathVariable("userId") Long userId, @PathVariable("lessonId") Long lessonId);

}
