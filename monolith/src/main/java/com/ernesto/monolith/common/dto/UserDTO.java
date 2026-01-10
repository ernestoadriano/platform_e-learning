package com.ernesto.monolith.common.dto;

import com.ernesto.monolith.user.model.enums.Role;
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
        private Role role;
}
