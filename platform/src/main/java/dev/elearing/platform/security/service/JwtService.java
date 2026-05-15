package dev.elearing.platform.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.elearing.platform.dto.AuthPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(AuthPrincipal principal) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(principal.getEmail())
                    .withClaim("id", principal.getId())
                    .withClaim("roles",
                            principal.getAuthorities()
                                    .stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .toList()
                    )
                    .withExpiresAt(new Date(getExpirationDate()))
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
            return exception.getMessage();
        }
    }

    private Long getExpirationDate() {
        return System.currentTimeMillis() + 15 * 60 * 1000;
    }
}
