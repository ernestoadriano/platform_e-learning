package com.org.auth_service.dto;

public record UserDTO(
        String name,
        String email,
        String password
) {
}
