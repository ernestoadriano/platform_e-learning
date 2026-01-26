package com.org.auth_service.dto;


public record AuthRequest(
        String email,
        String password
) {
}
