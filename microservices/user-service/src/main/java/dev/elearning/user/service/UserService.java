package dev.elearning.user.service;

import dev.elearning.user.dto.UserDTO;
import dev.elearning.user.model.User;
import dev.elearning.user.model.enums.Role;
import dev.elearning.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDTO create(UserDTO dto, Role role) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("This email already in use");
        }

        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encryptedPassword);
        user.setAvatar(dto.getAvatar());
        user.setRole(role);

        user = userRepository.save(user);

        return toDTO(user);
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public UserDTO getById(Long id) {
        return toDTO(getUserById(id));
    }

    public UserDTO update(Long id, UserDTO dto) {
        User user = getUserById(id);

        boolean isUpdated = false;

        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
            isUpdated = true;
        }

        if (dto.getEmail() != null) {
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new RuntimeException("This email is already in use");
            }
            user.setEmail(dto.getEmail());
            isUpdated = true;
        }

        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
            isUpdated = true;
        }

        if (dto.getPassword() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
            user.setPassword(encryptedPassword);
            isUpdated = true;
        }

        if (isUpdated) {
            user.setUpdatedAt(LocalDateTime.now());
        }

        user = userRepository.save(user);

        return toDTO(user);
    }

    public void delete(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public UserDTO getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return toDTO(user);
    }


    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatar(),
                user.getRole().getRole()

        );
    }
}
