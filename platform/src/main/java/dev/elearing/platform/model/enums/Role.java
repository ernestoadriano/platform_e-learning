package dev.elearing.platform.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("ADMIN"),
    USER("USER"),
    TEACHER("TEACHER");

    private final String role;
}
