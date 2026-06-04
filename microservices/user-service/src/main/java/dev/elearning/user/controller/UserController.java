package dev.elearning.user.controller;

import dev.elearning.user.dto.DTO;
import dev.elearning.user.dto.UserDTO;
import dev.elearning.user.model.enums.Role;
import dev.elearning.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/email")
    public UserDTO getByEmail(@RequestParam("email") String email) {
        return service.getByEmail(email);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PostMapping("register/admin")
    public UserDTO createAdmin(@RequestBody UserDTO dto) {
        return service.create(dto, Role.ADMIN);
    }

    @PostMapping("register/student")
    public UserDTO createStudent(@RequestBody UserDTO dto) {
        return service.create(dto, Role.STUDENT);
    }

    @PostMapping("register/teacher")
    public UserDTO createTeacher(@RequestBody UserDTO dto) {
        return service.create(dto, Role.TEACHER);
    }

    @PutMapping
    public UserDTO update(@RequestBody UserDTO dto, @PathVariable("id") Long id) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
