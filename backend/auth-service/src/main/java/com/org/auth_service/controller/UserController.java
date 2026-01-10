package com.org.auth_service.controller;

import com.org.auth_service.dto.UserDTO;
import com.org.auth_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAll();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody UserDTO dto) {
        return service.register(dto, "admin");
    }

    @PostMapping("/register/instructor")
    public ResponseEntity<?> registerInstructor(@RequestBody UserDTO dto) {
        return service.register(dto, "instructor");
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody UserDTO dto) {
        return service.register(dto, "student");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
}
