package dev.elearning.auth.security.service;

import dev.elearning.auth.client.UserClient;
import dev.elearning.auth.dto.DTO;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserClient userClient;

    public AuthorizationService(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        return userClient.getUserByEmail(email);
    }
}
