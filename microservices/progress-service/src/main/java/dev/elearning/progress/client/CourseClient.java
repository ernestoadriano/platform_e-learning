package dev.elearning.progress.client;

import dev.elearning.progress.dto.response.ModuleProgressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "course-service", url = "${course.service.url:http://localhost:8083}")
public interface CourseClient {

    @GetMapping("/api/courses/{courseId}/modules")
    List<ModuleProgressResponse> getModulesByCourseId(@PathVariable("courseId") Long courseId);

    @GetMapping("/api/courses/{courseId}/modules/{moduleId}/lessons/count")
    Long countLessonsByModule(@PathVariable("courseId") Long courseId, @PathVariable("moduleId") Long moduleId);

    @GetMapping("/api/modules/{moduleId}/course-id")
    Long getCourseIdByModuleId(@PathVariable("moduleId") Long moduleId);
}
