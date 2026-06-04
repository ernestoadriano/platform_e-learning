package dev.elearning.auth.dto;

public record AuthResponse(
        String accesToken,
        String refreshToken,
        UserDTO user
) {
}
