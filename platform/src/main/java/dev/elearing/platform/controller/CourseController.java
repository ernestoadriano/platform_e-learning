package dev.elearing.platform.controller;

import dev.elearing.platform.dto.CourseDTO;
import dev.elearing.platform.model.User;
import dev.elearing.platform.service.CourseService;
import dev.elearing.platform.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping("/user/purchased")
    public ResponseEntity<List<CourseDTO>> getUserPurchasedCourses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(courseService.getUserCourses(user.getId()));
    }

    @GetMapping("/teacher/courses")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseDTO>> getTeacherCourses(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(courseService.getTeacherCourses(user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.createCourse(courseDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Void> purchaseCourse(@PathVariable Long id, @AuthenticationPrincipal User user) {
        courseService.purchaseCourse(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<?> getCourseProgress(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(progressService.getCourseProgress(user.getId(), id));
    }

    @GetMapping("/{id}/has-purchased")
    public ResponseEntity<Boolean> hasPurchased(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(courseService.hasPurchased(user.getId(), id));
    }
}