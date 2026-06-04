package dev.elearning.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenResponse {

    private boolean valid;
    private String email;
    private Long userId;
    private String role;
}
