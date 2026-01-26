package com.org.enrollment_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.org.enrollment_service.dto.JwtUserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public JwtUserPrincipal validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("microservice")
                    .build()
                    .verify(token);
            Long userId = decodedJWT.getClaim("userId").asLong();
            String email = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            return new JwtUserPrincipal(userId, email, roles);
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("ERROR: " + exception.getMessage());
        }
    }

}
