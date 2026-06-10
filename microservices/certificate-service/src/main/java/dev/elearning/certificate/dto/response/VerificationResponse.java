package dev.elearning.certificate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerificationResponse {

    private Boolean valid;
    private String certificateCode;
    private String userName;
    private String courseTitle;
    private LocalDateTime issuedAt;
    private Double grade;
    private String verifiedAt;
}
