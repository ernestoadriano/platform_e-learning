package dev.elearing.platform.controller;

import dev.elearing.platform.dto.AuthResponse;
import dev.elearing.platform.dto.LoginRequest;
import dev.elearing.platform.dto.RefreshRequest;
import dev.elearing.platform.model.User;
import dev.elearing.platform.security.service.AccessToken;
import dev.elearing.platform.service.RefreshTokenService;
import dev.elearing.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccessToken accessToken;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    /*@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication auth = authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        assert user != null;
        String token = accessToken.generateToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(user.getId());
        System.out.println(refreshToken);

        return ResponseEntity.ok(new AuthResponse(token, refreshToken, userService.toDTO(user)));
    }*/
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
       try {
           System.out.println("=== LOGIN CALLED ===");
           System.out.println("Email: " + request.email());

           var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());

           Authentication auth = authenticationManager.authenticate(usernamePassword);

           User user = (User) auth.getPrincipal();

           assert user != null;
           String token = accessToken.generateToken(user);
           String refreshToken = refreshTokenService.generateRefreshToken(user.getId());

           return ResponseEntity.ok(new AuthResponse(token, refreshToken, userService.toDTO(user)));
       } catch (Exception e) {
           System.out.println("Error on login: " + e.getMessage());
           return ResponseEntity.status(401).body("Authentication failed" + e.getMessage());
       }
    }

    @PostMapping("/test-auth")
    public ResponseEntity<?> testAuth(@RequestBody LoginRequest request) {
        try {
            System.out.println("Testing authentication for: " + request.email());
            var auth = new UsernamePasswordAuthenticationToken(request.email(),request.password());
            authenticationManager.authenticate(auth);
            return ResponseEntity.ok("Authentication is OK.");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        var refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        String newAccessToken = accessToken.generateToken(refreshToken.getUser());

        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.refreshToken(), userService.toDTO(refreshToken.getUser())));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}