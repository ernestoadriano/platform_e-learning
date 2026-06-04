package dev.elearning.auth.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.elearning.auth.dto.DecodedTokenInfo;
import dev.elearning.auth.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class AccessTokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(UserDTO user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withClaim("fullName", user.getFullName())
                    .withClaim("avatar", user.getAvatar())
                    .withClaim("role", user.getRole())
                    .withExpiresAt(Date.from(getExpirationDate()))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public DecodedTokenInfo validateAndDecode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decoded = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token);

            DecodedTokenInfo info = new DecodedTokenInfo();
            info.setEmail(decoded.getSubject());
            info.setUserId(decoded.getClaim("id").asLong());
            info.setName(decoded.getClaim("name").asString());
            info.setAvatar(decoded.getClaim("avatar").asString());
            info.setRole(decoded.getClaim("role").asString());

            return info;
        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("+02:00"));
    }
}
