package dev.elearning.certificate.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String avatar;
    private String role;
}
