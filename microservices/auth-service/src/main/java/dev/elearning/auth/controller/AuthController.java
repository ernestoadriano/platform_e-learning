package dev.elearning.auth.controller;

import dev.elearning.auth.dto.*;
import dev.elearning.auth.security.service.AccessTokenService;
import dev.elearning.auth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());

            Authentication auth = authenticationManager.authenticate(usernamePassword);

            UserDTO user = (UserDTO) auth.getPrincipal();

            assert user != null;
            String token = accessTokenService.generateToken(user);
            String refreshToken = refreshTokenService.generateRefreshToken(user.getId());

            return ResponseEntity.ok(new AuthResponse(token, refreshToken, user));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Authentication failed: " + exception.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        var refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        UserDTO user = refreshTokenService.getByUserId(refreshToken.getId());
        String newAccessToken = accessTokenService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken(), user));
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestHeader(AUTHORIZATION) String authHeader) {

        String token = authHeader.substring(7);
        try {
            var decodedToken = accessTokenService.validateAndDecode(token);

            ValidateTokenResponse response = new ValidateTokenResponse();
            response.setValid(true);
            response.setEmail(decodedToken.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            ValidateTokenResponse response = new ValidateTokenResponse();
            response.setValid(false);
            return ResponseEntity.ok(response);
        }
    }
}
