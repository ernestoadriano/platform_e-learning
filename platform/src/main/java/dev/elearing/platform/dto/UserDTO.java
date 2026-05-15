package dev.elearing.platform.dto;

import dev.elearing.platform.model.enums.Role;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private String name;

    private String email;

    private String password;

    private String avatar;

    private Role role;
}
