package com.org.auth_service.dto;

public record AuthenticationDTO(
        String email,
        String password
) {
}
