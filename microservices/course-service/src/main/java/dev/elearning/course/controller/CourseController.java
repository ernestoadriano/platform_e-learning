package dev.elearning.course.controller;

import dev.elearning.course.dto.request.CourseRequest;
import dev.elearning.course.dto.response.CourseResponse;
import dev.elearning.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(courseService.getAllPublishedCourses());
    }

    @GetMapping("/caegory/{categoryId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(courseService.getCoursesByCategory(categoryId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByInstructor(@PathVariable("instructorId") Long instructorId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(instructorId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<CourseResponse>> getRecentCourses(@RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ResponseEntity.ok(courseService.getRecentCourse(limit));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<CourseResponse>> getMostPopularCourses(@RequestParam(defaultValue = "10", name = "limit") int limit) {
        return ResponseEntity.ok(courseService.getMostPopularCourses(limit));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request) {
        CourseResponse response = courseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(@PathVariable("id") Long id, @RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<CourseResponse> publishCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.publishedCourse(id));
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<CourseResponse> unpublishCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok(courseService.unpublishCourse(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        courseService.delete(id);
    }
}
