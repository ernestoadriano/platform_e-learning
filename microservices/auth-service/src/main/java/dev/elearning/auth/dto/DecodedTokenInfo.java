package dev.elearning.auth.dto;

import lombok.Data;

@Data
public class DecodedTokenInfo {
    private String email;
    private Long userId;
    private String name;
    private String avatar;
    private String role;
}
