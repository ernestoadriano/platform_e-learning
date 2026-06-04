package dev.elearning.course.controller;

import dev.elearning.course.dto.request.ModuleRequest;
import dev.elearning.course.dto.response.ModuleResponse;
import dev.elearning.course.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @GetMapping
    public ResponseEntity<List<ModuleResponse>> getModuleByCourse(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(moduleService.getModulesByCourse(courseId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> getModulesById(@PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(moduleService.getModuleById(moduleId));
    }

    @PostMapping
    public ResponseEntity<ModuleResponse> create(@PathVariable("courseId") Long courseId, @RequestBody ModuleRequest request) {
        ModuleResponse response = moduleService.create(courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponse> update(@PathVariable("moduleId") Long moduleId, @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.update(moduleId, request));
    }

    @PatchMapping("/reorder")
    public ResponseEntity<Void> reorderModules(@PathVariable("courseId") Long courseId, @RequestBody List<Long> moduleIds) {
        moduleService.reorderModules(courseId, moduleIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<Void> deleteModule(@PathVariable("moduleId") Long moduleId) {
        moduleService.delete(moduleId);
        return ResponseEntity.noContent().build();
    }
}
