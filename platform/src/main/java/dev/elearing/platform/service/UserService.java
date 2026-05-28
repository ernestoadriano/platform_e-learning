package dev.elearing.platform.service;

import dev.elearing.platform.dto.UserDTO;
import dev.elearing.platform.model.User;
import dev.elearing.platform.model.enums.Role;
import dev.elearing.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
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
            throw new RuntimeException("This email already in use.");
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(encryptedPassword);
        user.setAvatar(dto.getAvatar());
        user.setRole(role);
        user.setIsActive(true);

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

   /* public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }*/

    public UserDTO update(Long id, UserDTO dto) {
        User user = getUserById(id);

        boolean isUpdated = false;

        if (dto.getName() != null) {
            user.setName(dto.getName());
            isUpdated = true;
        }

        if (dto.getEmail() != null) {
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new RuntimeException("This email is already in use");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
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

    @Transactional
    public void updateLastLogin(Long id) {
        User user = getUserById(id);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }


    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getName(),
                user.getEmail(),
                "-",
                user.getAvatar(),
                user.getRole().getRole()
        );
    }
}
