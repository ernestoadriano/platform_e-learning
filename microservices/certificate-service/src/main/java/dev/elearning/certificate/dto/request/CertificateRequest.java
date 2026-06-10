package dev.elearning.certificate.dto.request;

import lombok.Data;

@Data
public class CertificateRequest {

   private Long userId;
   private Long courseId;
   private Double grade;
}
