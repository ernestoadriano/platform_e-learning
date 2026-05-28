package dev.elearing.platform.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserDTO user
) {
}
