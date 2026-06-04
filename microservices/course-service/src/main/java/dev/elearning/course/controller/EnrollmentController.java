package dev.elearning.course.controller;

import dev.elearning.course.dto.request.EnrollmentRequest;
import dev.elearning.course.dto.response.EnrollmentResponse;
import dev.elearning.course.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollUser(@RequestBody EnrollmentRequest request) {
        EnrollmentResponse response = enrollmentService.enrollUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<EnrollmentResponse>> getUserEnrollments(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(enrollmentService.getUserEnrollments(userId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isUserEnrolled(@RequestParam("userId") Long userId, @RequestParam("courseId") Long courseId) {
        return ResponseEntity.ok(enrollmentService.isUserEnrolled(userId, courseId));
    }

    @GetMapping("/courses/{courseId}/count")
    public ResponseEntity<Long> countEnrolledStudents(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(enrollmentService.countEnrolledStudents(courseId));
    }

    @DeleteMapping("/{courseId}/users/{userId}")
    public ResponseEntity<Void> cancelEnrollment(@PathVariable("userId") Long userId, @PathVariable("courseId") Long courseId) {
        enrollmentService.cancelEnrollment(userId, courseId);
        return ResponseEntity.noContent().build();
    }
 }
