package dev.elearing.platform.controller;

import dev.elearing.platform.dto.*;
import dev.elearing.platform.model.RefreshToken;
import dev.elearing.platform.model.User;
import dev.elearing.platform.repository.UserRepository;
import dev.elearing.platform.security.service.JwtService;
import dev.elearing.platform.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        assert userDetails != null;
        var user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken((AuthPrincipal) userDetails);
        String refreshToken = refreshTokenService.generateRefreshToken(user.getId());

        return ResponseEntity.ok(new AuthResponse(
                accessToken,
                refreshToken,
                user.getName(),
                user.getEmail(),
                user.getRole().getRole(),
                user.getAvatar()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());

        var user = refreshToken.getUser();

        UserDetails userDetails = toPrincipal(user);

        String newAccessToken = jwtService.generateToken((AuthPrincipal) userDetails);

        refreshTokenService.revokeToken(refreshToken);
        String newRefreshToken = refreshTokenService.generateRefreshToken(user.getId());

        return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
    }

    private UserDetails toPrincipal(User user) {
        return new AuthPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatar(),
                user.getRole()
        );
    }
}
