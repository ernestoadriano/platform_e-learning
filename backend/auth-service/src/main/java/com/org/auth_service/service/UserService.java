package com.org.auth_service.service;

import com.org.auth_service.dto.UserDTO;
import com.org.auth_service.model.User;
import com.org.auth_service.model.enums.Role;
import com.org.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;

    public List<UserDTO> getAll() {
        Sort sort = Sort.by("name");
        List<User> users = repository.findAll(sort);
        List<UserDTO> dtoList = new ArrayList<>();
        for (User user : users) {
            dtoList.add(new UserDTO(user.getName(), user.getEmail(), null));
        }

        return dtoList;
    }
    
    public ResponseEntity<?> register(UserDTO dto, String role) {
        if (repository.findByEmail(dto.email()) != null) {
            return ResponseEntity.badRequest().body("This email is already exist");
        }

        User user = new User();
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encryptedPassword);

        if (role.equals("admin"))
            user.setRole(Role.ADMIN);
        else if (role.equals("instructor"))
            user.setRole(Role.INSTRUCTOR);
        else
            user.setRole(Role.STUDENT);

        repository.save(user);

        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<?> update(Long id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this user"));
        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.email() != null) {
            if (repository.findByEmail(dto.email()) != null) {
                return ResponseEntity.badRequest().body("This email already exist");
            }
            user.setEmail(dto.email());
        }

        if (dto.password() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            user.setPassword(encryptedPassword);
        }

        repository.save(user);

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found this user"));

        repository.delete(user);

        return ResponseEntity.ok(getAll());
    }
}
