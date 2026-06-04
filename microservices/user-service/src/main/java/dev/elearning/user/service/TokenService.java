package dev.elearning.user.service;

import dev.elearning.user.client.AuthClient;
import dev.elearning.user.dto.ValidateTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenService {

    private AuthClient authClient;

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ValidateTokenResponse validateToken(String token) {
        try {
            return authClient.validateToken("Bearer " + token);
        } catch (Exception exception) {
            ValidateTokenResponse response = new ValidateTokenResponse();
            response.setValid(false);
            return response;
        }
    }

    public String extractEmailFromToken(String token) {
        ValidateTokenResponse response = validateToken(token);
        return response.isValid() ? response.getEmail() : null;
    }
}
