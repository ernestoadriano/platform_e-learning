package dev.elearing.platform.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        String name,
        String email,
        String role,
        String avatar
) {
}
