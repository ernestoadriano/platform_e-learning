package dev.elearing.platform.dto;

public record LoginRequest(
        String email,
        String password
) {
}
