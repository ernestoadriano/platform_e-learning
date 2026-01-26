package com.org.user_service.model.enums;

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
