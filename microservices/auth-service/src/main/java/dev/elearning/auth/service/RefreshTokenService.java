package dev.elearning.auth.service;

import dev.elearning.auth.client.UserClient;
import dev.elearning.auth.dto.UserDTO;
import dev.elearning.auth.model.RefreshToken;
import dev.elearning.auth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final UserClient userClient;

    public RefreshTokenService(UserClient userClient) {
        this.userClient = userClient;
    }

    public String generateRefreshToken(Long userId) {
        UserDTO user = userClient.getUserById(userId);

        refreshTokenRepository.findByUserId(userId).ifPresent(token -> {
            refreshTokenRepository.delete(token);
        });

        String refreshToken = UUID.randomUUID().toString();
        String hash = BCrypt.hashpw(refreshToken, BCrypt.gensalt());

        RefreshToken entity = new RefreshToken();
        entity.setUserId(user.getId());
        entity.setTokenHash(hash);
        entity.setExpiryDate(LocalDateTime.now().plusDays(30));
        entity.setRevoked(false);
        refreshTokenRepository.save(entity);

        return refreshToken;
    }

    public RefreshToken validateRefreshToken(String refreshToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findAll();

        for (RefreshToken token : tokens) {
            if (BCrypt.checkpw(refreshToken, token.getTokenHash())) {
                if (token.getRevoked()) {
                    throw new RuntimeException("Refresh token revoked");
                }

                if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("Refresh token expired");
                }

                return token;
            }
        }

        throw new RuntimeException("Invalid refresh token");
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public UserDTO getByUserId(Long id) {
        return userClient.getUserById(id);
    }
}
