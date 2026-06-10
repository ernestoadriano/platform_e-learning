package dev.elearning.certificate.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CertificateResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long courseId;
    private String courseTitle;
    private String certificateCode;
    private LocalDateTime issuedAt;
    private String certificateUrl;
    private Double grade;
    private Integer downloadCount;
    private String verificationUrl;
}
