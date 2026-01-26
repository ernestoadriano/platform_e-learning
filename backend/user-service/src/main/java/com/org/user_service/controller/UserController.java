package com.org.user_service.controller;

import com.org.user_service.dto.JwtUserPrincipal;
import com.org.user_service.dto.UserDTO;
import com.org.user_service.model.User;
import com.org.user_service.model.enums.Role;
import com.org.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register/admin")
    public UserDTO registerAdmin(@RequestBody UserDTO dto) {
        dto.setRole(Role.ADMIN);
        var user = service.register(dto);
        return toDTO(user);
    }

    @PostMapping("/register/instructor")
    public UserDTO registerInstructor(@RequestBody UserDTO dto) {
        dto.setRole(Role.INSTRUCTOR);
        var user = service.register(dto);
        return toDTO(user);
    }

    @PostMapping("/register/student")
    public UserDTO registerStudent(@RequestBody UserDTO dto) {
        dto.setRole(Role.STUDENT);
        var user = service.register(dto);
        return toDTO(user);
    }

    @GetMapping
    public List<UserDTO> getAll() {
        List<User> users = service.getAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(toDTO(user));
        }

        return userDTOS;
    }

    @GetMapping("/email/{email}")
    public User getByEmail(@PathVariable("email") String email) {
        return service.getByEmail(email);
    }

    @PutMapping
    public UserDTO update(@RequestBody UserDTO dto, @AuthenticationPrincipal JwtUserPrincipal principal) {
        var userUpdated = service.update(principal.getUserId(), dto);
        return toDTO(userUpdated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }
}
