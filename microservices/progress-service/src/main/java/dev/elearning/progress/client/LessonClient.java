package dev.elearning.progress.client;

import dev.elearning.progress.dto.response.LessonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "lesson-service", url = "${lesson.service.url:http://localhost:8084}")
public interface LessonClient {

    @GetMapping("/api/lessons/module/{moduleId}/count")
    Long countLessonsByModule(@PathVariable("moduleId") Long moduleId);

    @GetMapping("/api/lessons/module/{moduleId}")
    List<LessonResponse> getLessonsByModule(@PathVariable("moduleId") Long moduleId);
}
