package dev.elearing.platform.service;

import dev.elearing.platform.model.RefreshToken;
import dev.elearing.platform.model.User;
import dev.elearing.platform.repository.RefreshTokenRepository;
import dev.elearing.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public String generateRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String refreshToken = UUID.randomUUID() + "-" + UUID.randomUUID();

        String hash = BCrypt.hashpw(refreshToken, BCrypt.gensalt());

        RefreshToken entity = new RefreshToken();
        entity.setUser(user);
        entity.setTokenHash(hash);
        entity.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        entity.setRevoked(false);
        refreshTokenRepository.save(entity);

        return refreshToken;
    }

    public RefreshToken validateRefreshToken(String refreshToken) {
        List<RefreshToken> tokens = refreshTokenRepository.findAll();

        for (RefreshToken token : tokens) {
            if (BCrypt.checkpw(refreshToken, token.getTokenHash())) {
                if (token.getRevoked())
                    throw new RuntimeException("Refresh token revoked");

                if (token.getExpiryDate().isBefore(Instant.now()))
                    throw new RuntimeException("Refresh token expired");

                return token;
            }
        }

        throw new RuntimeException("Invalid refresh token");
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}
