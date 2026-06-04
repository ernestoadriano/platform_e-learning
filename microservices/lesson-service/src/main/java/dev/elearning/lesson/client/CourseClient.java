package dev.elearning.lesson.client;

import dev.elearning.lesson.dto.response.ModuleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${course.service.url:http://localhost:8083}")
public interface CourseClient {

    @GetMapping("/api/courese/{courseId{/modules/{moduleId}")
    ModuleResponse getModuleById(@PathVariable("courseId") Long courseId, @PathVariable("moduleId") Long moduleId);
}
