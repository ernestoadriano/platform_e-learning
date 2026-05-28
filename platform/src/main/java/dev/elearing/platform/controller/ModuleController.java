package dev.elearing.platform.controller;

import dev.elearing.platform.dto.ModuleDTO;
import dev.elearing.platform.model.User;
import dev.elearing.platform.service.ModuleService;
import dev.elearing.platform.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses/{courseId}/modules")
@CrossOrigin(origins = "*")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<ModuleDTO>> getModulesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(moduleService.getAllByCourse(courseId));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleDTO> getModuleById(@PathVariable Long moduleId) {
        return ResponseEntity.ok(moduleService.getById(moduleId));
    }

    @GetMapping("/{moduleId}/can-access")
    public ResponseEntity<Boolean> canAccessModule(@PathVariable Long moduleId, @AuthenticationPrincipal User user) {
        boolean canAccess = progressService.canAccessModule(user.getId(), moduleId);
        return ResponseEntity.ok(canAccess);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ModuleDTO> createModule(@PathVariable Long courseId, @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.ok(moduleService.createModule(courseId, moduleDTO));
    }

    @PutMapping("/{moduleId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<ModuleDTO> updateModule(@PathVariable Long moduleId, @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, moduleDTO));
    }

    @DeleteMapping("/{moduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.ok().build();
    }
}