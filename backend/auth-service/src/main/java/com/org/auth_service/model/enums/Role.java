package com.org.auth_service.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Role {

    INSTRUCTOR("ROLE_INSTRUCTOR"),
    ADMIN("ROLE_ADMIN"),
    STUDENT("ROLE_STUDENT");

    private final String role;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this == Role.ADMIN) {
            return List.of(new SimpleGrantedAuthority(this.role),
                    new SimpleGrantedAuthority(INSTRUCTOR.role),
                    new SimpleGrantedAuthority(STUDENT.role));
        }

        return List.of(new SimpleGrantedAuthority(this.role));
    }
}
