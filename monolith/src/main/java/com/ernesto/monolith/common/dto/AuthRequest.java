package com.ernesto.monolith.common.dto;

public record AuthRequest(
        String email,
        String password
) {
}
