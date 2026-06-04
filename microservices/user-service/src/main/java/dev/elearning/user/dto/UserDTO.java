package dev.elearning.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String avatar;

    private String role;
}
