package com.ernesto.monolith.user.controller;

import com.ernesto.monolith.common.dto.UserDTO;
import com.ernesto.monolith.user.model.User;
import com.ernesto.monolith.user.model.enums.Role;
import com.ernesto.monolith.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PostMapping("/admin")
    public UserDTO createAdmin(@RequestBody UserDTO dto) {
        dto.setRole(Role.ADMIN);
        return service.create(dto);
    }

    @PostMapping("/instructor")
    public UserDTO createInstructor(@RequestBody UserDTO dto) {
        dto.setRole(Role.INSTRUCTOR);
        return service.create(dto);
    }

    @PostMapping("/student")
    public UserDTO createStudent(@RequestBody UserDTO dto) {
        dto.setRole(Role.STUDENT);
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable("id") Long id, @RequestBody UserDTO dto, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id)) {
            throw new RuntimeException("Forbidden");
        }
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public List<UserDTO> delete(@PathVariable("id") Long id) {
        return service.delete(id);
    }
}
