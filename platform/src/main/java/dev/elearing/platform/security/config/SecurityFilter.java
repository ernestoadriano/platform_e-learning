package dev.elearing.platform.security.config;

import dev.elearing.platform.repository.UserRepository;
import dev.elearing.platform.security.service.AccessToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private AccessToken accessToken;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);

        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = accessToken.validateToken(token);

        if (email != null && !email.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userRepository.findByEmail(email);

            if (user != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
        /*if (token != null) {
            String username = accessToken.validateToken(token);
            UserDetails user = userRepository.findByEmail(username);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);*/
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
}
