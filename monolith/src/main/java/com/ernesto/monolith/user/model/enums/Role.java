package com.ernesto.monolith.user.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    ADMIN("ADMIN"),
    INSTRUCTOR("INSTRUCTOR"),
    STUDENT("STUDENT");

    private final String role;
}
