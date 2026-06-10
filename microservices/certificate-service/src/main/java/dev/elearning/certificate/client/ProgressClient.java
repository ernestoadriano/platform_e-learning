package dev.elearning.certificate.client;

import dev.elearning.certificate.dto.response.CourseProgressResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "progress-service", url = "${progress.service.url:http:localhost:8086}")
public interface ProgressClient {

    @GetMapping("/api/progress/courses/users/{userId}/courses/{courseId}")
    CourseProgressResponse getCourseProgress(@PathVariable("userId") Long userId, @PathVariable("courseId") Long courseId);
}
