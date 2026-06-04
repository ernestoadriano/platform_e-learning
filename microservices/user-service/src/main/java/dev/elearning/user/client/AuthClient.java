package dev.elearning.user.client;

import dev.elearning.user.dto.ValidateTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@FeignClient(name = "authClient", url = "${auth.service.url:http://localhost:8081}/api/auth")
public interface AuthClient {

    @GetMapping("/validate")
    ValidateTokenResponse validateToken(@RequestHeader(AUTHORIZATION) String token);
}

