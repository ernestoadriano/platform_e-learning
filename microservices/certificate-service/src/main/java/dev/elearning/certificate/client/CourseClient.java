package dev.elearning.certificate.client;

import dev.elearning.certificate.dto.response.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "course-service", url = "${course.service.url:http://localhost:8083}")
public interface CourseClient {

    @GetMapping("/api/courses/{courseId}")
    CourseResponse getCourseById(@PathVariable("courseId") Long courseId);
}
