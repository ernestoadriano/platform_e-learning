package com.org.auth_service.controller;

import com.org.auth_service.dto.AuthRequest;
import com.org.auth_service.dto.AuthResponse;
import com.org.auth_service.dto.UserPrincipal;
import com.org.auth_service.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(usernamePassword);
        String token = tokenService.generateToken((UserPrincipal) Objects.requireNonNull(authentication.getPrincipal()));
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
