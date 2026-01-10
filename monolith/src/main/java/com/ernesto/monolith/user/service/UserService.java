package com.ernesto.monolith.user.service;

import com.ernesto.monolith.common.dto.UserDTO;
import com.ernesto.monolith.user.model.User;
import com.ernesto.monolith.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user.getName(), user.getEmail(), "", user.getRole()));
        }
        return usersDTO;
    }

    public UserDTO getById(Long id) {
        User user = getUserById(id);
        return toDTO(user);
    }

    public UserDTO create(UserDTO dto) {
        if (repository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("Email already exists.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        User user = new User(null, dto.getName(), dto.getEmail(), encryptedPassword, dto.getRole());

        repository.save(user);
        return toDTO(user);
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = getUserById(id);

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            if (!user.getEmail().equals(dto.getEmail())) {
                if (repository.findByEmail(dto.getEmail()) != null) {
                    throw new RuntimeException("Email already exists");
                }
            }

            user.setEmail(dto.getEmail());
        }

        if (dto.getEmail() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
            user.setPassword(encryptedPassword);
        }
        repository.save(user);
        return toDTO(user);
    }

    public List<UserDTO> delete(Long id) {
        User user = getUserById(id);
        repository.delete(user);
        return getAll();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getName(), user.getEmail(), "", user.getRole());
    }

    private User getUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not exists"));
    }
}
