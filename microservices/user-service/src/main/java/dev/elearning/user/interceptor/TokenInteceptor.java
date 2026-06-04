package dev.elearning.user.interceptor;

import dev.elearning.user.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class TokenInteceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            var validation = tokenService.validateToken(token);

            if (!validation.isValid()) {
                response.setStatus(401);
                return false;
            }

            request.setAttribute("userId", validation.getUserId());
            request.setAttribute("userEmail", validation.getEmail());
            request.setAttribute("userRole", validation.getRole());
        }

        return true;
    }
}
