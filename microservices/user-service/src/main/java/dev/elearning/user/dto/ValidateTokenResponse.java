package dev.elearning.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenResponse {

    private boolean valid;
    private String email;
    private Long userId;
    private String role;
}
