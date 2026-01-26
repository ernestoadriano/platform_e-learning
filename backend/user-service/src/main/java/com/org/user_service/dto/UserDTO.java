package com.org.user_service.dto;

import com.org.user_service.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO{

        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;

        public UserDTO(String name, String email, String password, Role role) {
                this.name = name;
                this.email = email;
                this.password = password;
                this.role = role;
        }
}
