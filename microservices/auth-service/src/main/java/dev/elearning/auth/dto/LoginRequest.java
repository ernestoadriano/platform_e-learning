package dev.elearning.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
