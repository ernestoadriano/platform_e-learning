package dev.elearing.platform.dto;


public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
