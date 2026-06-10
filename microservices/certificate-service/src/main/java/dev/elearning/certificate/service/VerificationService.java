package dev.elearning.certificate.service;

import dev.elearning.certificate.client.CourseClient;
import dev.elearning.certificate.client.UserClient;
import dev.elearning.certificate.dto.response.VerificationResponse;
import dev.elearning.certificate.model.Certificate;
import dev.elearning.certificate.repository.CertificateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class VerificationService {

    @Autowired
    private CertificateRepository certificateRepository;

    private UserClient userClient;
    private CourseClient courseClient;

    public VerificationResponse verifyCertificate(String certificateCode) {
        log.info("Verifying certificate: {}", certificateCode);

        VerificationResponse response = new VerificationResponse();
        response.setVerifiedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        try {
            Certificate certificate = certificateRepository.findByCertificateCode(certificateCode)
                    .orElse(null);

            if (certificate == null) {
                response.setValid(false);
                response.setCertificateCode(certificateCode);
                return response;
            }

            var user = userClient.getUserById(certificate.getUserId());
            var course = courseClient.getCourseById(certificate.getCourseId());

            response.setValid(true);
            response.setCertificateCode(certificate.getCertificateCode());
            response.setUserName(user != null ? user.getName() : null);
            response.setCourseTitle(course != null ? course.getTitle() : null);
            response.setIssuedAt(certificate.getIssuedAt());
            response.setGrade(certificate.getGrade());
        } catch (Exception e) {
            log.error("Error verifying certificate: {}", certificateCode, e);
            response.setValid(false);
        }

        return response;
    }
}
