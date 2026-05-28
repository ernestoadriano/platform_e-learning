package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateResponse {

    private Long id;
    private String courseTitle;
    private String userName;
    private LocalDateTime completionDate;
    private String certificateCode;
    private String certificateUrl;
}
