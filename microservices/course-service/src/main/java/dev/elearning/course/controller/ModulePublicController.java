package dev.elearning.course.controller;

import dev.elearning.course.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/modules")
public class ModulePublicController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping("/{moduleId}/course-id")
    public ResponseEntity<Long> getCourseIdByModuleId(@PathVariable("moduleId") Long moduleId) {
        Long courseId = moduleService.getCourseIdByModuleId(moduleId);
        return ResponseEntity.ok(courseId);
    }
}
