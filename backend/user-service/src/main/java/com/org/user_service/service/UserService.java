package com.org.user_service.service;

import com.org.user_service.dto.UserDTO;
import com.org.user_service.model.User;
import com.org.user_service.repository.UserRepository;
import com.org.user_service.utils.Validations;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Transactional
    public User register(UserDTO dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Validations.validateEmail(dto.getEmail());

        Validations.validatePassword(dto.getPassword());

        Validations.validateName(dto.getName());

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encryptedPassword);
        user.setRole(dto.getRole());
        return repository.save(user);
    }

    public List<User> getAll() {
        Sort sort = Sort.by("name");
        return repository.findAll(sort);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User update(Long id, UserDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getName() != null) {
            Validations.validateName(dto.getName());
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            Validations.validateEmail(dto.getEmail());
            if (repository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            Validations.validatePassword(dto.getPassword());
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
            user.setPassword(encryptedPassword);
        }

        user.setUpdatedAt(LocalDateTime.now());

        return repository.save(user);
    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        repository.delete(user);
    }
}
