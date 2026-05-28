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
import java.util.Random;
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

        refreshTokenRepository.findByUserId(userId).ifPresent(token -> {
            refreshTokenRepository.delete(token);
        });
        String refreshToken = UUID.randomUUID().toString();
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

    private StringBuilder generateToken() {
        String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz123456789.!@#$%^&*()_+=-*/,.;:}{[]<>?";
        Random random = new Random();
        StringBuilder token = new StringBuilder();
        int index;
        for (int i = 0; i < 80; i++) {
            index = random.nextInt(digits.length());
            token.append(digits.charAt(index));
        }

        return token;
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }
}
