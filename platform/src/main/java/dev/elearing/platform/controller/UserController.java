package dev.elearing.platform.controller;

import dev.elearing.platform.dto.UserDTO;
import dev.elearing.platform.model.enums.Role;
import dev.elearing.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register/admin")
    public UserDTO createAdmin(@RequestBody UserDTO dto) {
        return service.create(dto, Role.ADMIN);
    }

    @PostMapping("/register/teacher")
    public UserDTO createTeacher(@RequestBody UserDTO dto) {
        return service.create(dto, Role.TEACHER);
    }

    @PostMapping("/register/student")
    public UserDTO createStudent(@RequestBody UserDTO dto) {
        return service.create(dto, Role.USER);
    }
    @GetMapping
    public List<UserDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable("id") Long id, @RequestBody UserDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
