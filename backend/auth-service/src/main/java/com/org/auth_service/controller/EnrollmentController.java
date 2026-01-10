package com.org.auth_service.controller;

import com.org.auth_service.dto.EnrollmentDTO;
import com.org.auth_service.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EnrollmentDTO dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public EnrollmentDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}/progress")
    public EnrollmentDTO updateProgress(@PathVariable("id") Long id, @RequestParam("plusProgress") Double plusProgress) {
        return service.updateProgress(id, plusProgress);
    }
}
