package dev.elearning.certificate.service;

import dev.elearning.certificate.client.CourseClient;
import dev.elearning.certificate.client.ProgressClient;
import dev.elearning.certificate.client.UserClient;
import dev.elearning.certificate.dto.request.CertificateRequest;
import dev.elearning.certificate.dto.response.CertificateResponse;
import dev.elearning.certificate.dto.response.CourseProgressResponse;
import dev.elearning.certificate.dto.response.CourseResponse;
import dev.elearning.certificate.dto.response.UserResponse;
import dev.elearning.certificate.model.Certificate;
import dev.elearning.certificate.repository.CertificateRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    private ProgressClient progressClient;
    private UserClient userClient;
    CourseClient courseClient;

    @Transactional
    public CertificateResponse generateCertificate(CertificateRequest request) {
        log.info("Generating certificate for user: {}, course: {}", request.getUserId(),request.getCourseId());
        if (certificateRepository.existsByUserIdAndCourseId(request.getUserId(), request.getCourseId())) {
            throw new RuntimeException("Certificate already exists fot this user and course");
        }

        CourseProgressResponse progress = progressClient.getCourseProgress(request.getUserId(), request.getCourseId());

        if (progress.getPercentage() == null || progress.getPercentage() < 99.9) {
            throw new RuntimeException("Course not completed yet. Progress: " + progress.getPercentage() + "%");
        }

        UserResponse user = userClient.getUserById(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }

        CourseResponse course = courseClient.getCourseById(request.getCourseId());

        if (course == null) {
            throw new RuntimeException("Course not found: " + request.getCourseId());
        }

        String certificateCode = generateCertificateCode(request.getUserId(), request.getCourseId());

        String verificationUrl = generateVerificationUrl(certificateCode);

        Certificate certificate = new Certificate();
        certificate.setUserId(request.getUserId());
        certificate.setCourseId(request.getCourseId());
        certificate.setCertificateCode(certificateCode);
        certificate.setVerificationUrl(verificationUrl);
        certificate.setCertificateUrl(null);
        certificate.setGrade(request.getGrade());
        certificate.setDownloadCount(0);

        certificate = certificateRepository.save(certificate);

        log.info("Certificate generated successfully: {}", certificateCode);

        return toResponse(certificate, user, course);
    }

    public CertificateResponse getCertificateById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificate not found: " + id));

        UserResponse user = userClient.getUserById(certificate.getUserId());
        CourseResponse course = courseClient.getCourseById(certificate.getCourseId());
        return toResponse(certificate, user, course);
    }

    public CertificateResponse getCertificateByCode(String  certificateCode) {
        Certificate certificate = certificateRepository.findByCertificateCode(certificateCode)
                .orElseThrow(() -> new RuntimeException("Certificate not found: " + certificateCode));

        UserResponse user = userClient.getUserById(certificate.getUserId());
        CourseResponse course = courseClient.getCourseById(certificate.getCourseId());

        return toResponse(certificate, user, course);
    }

    public Page<CertificateResponse> getCertificateByUserIdPaginated(Long userId, Pageable pageable) {

        return certificateRepository.findAllByUserId(userId, pageable)
                .map(certificate -> {
                   UserResponse user = userClient.getUserById(certificate.getUserId());
                   CourseResponse course = courseClient.getCourseById(certificate.getCourseId());
                   return toResponse(certificate, user, course);
                });
    }

    public boolean hasCertificate(Long userId, Long courseId) {
        return certificateRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    public CertificateResponse getUserCertificateForCourse(Long userId, Long courseId) {
        Certificate certificate = certificateRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("Certificate not found for user: " + userId + " and course: " + courseId));
        UserResponse user = userClient.getUserById(certificate.getUserId());
        CourseResponse course = courseClient.getCourseById(certificate.getCourseId());

        return toResponse(certificate, user, course);
    }

    @Transactional
    public void incrementDownloadCount(Long certificateId) {
        certificateRepository.incrementDownoloadCount(certificateId);
    }

    @Transactional
    public void delete(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new RuntimeException("Certificate not found: " + id);
        }

        certificateRepository.deleteById(id);
        log.info("Certificate delete: {}", id);
    }

    @Transactional
    public void deleteCertificatesByUserId(Long userId) {
        long count = certificateRepository.countByUserId(userId);
        certificateRepository.deleteByUserId(userId);
        log.info("Deleted {} certificates for user: {}", count, userId);
    }

    public Long countCertificatesByUserId(Long userId) {
        return certificateRepository.countByUserId(userId);
    }

    private String generateCertificateCode(Long userId, Long courseId) {
        return "CERT-" + userId + "-" + courseId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateVerificationUrl(String certificateCode) {
        return "/api/certificates/verifiy/" + certificateCode;
    }

    private CertificateResponse toResponse(Certificate certificate, UserResponse user, CourseResponse course) {
        CertificateResponse response = new CertificateResponse();
        response.setId(certificate.getId());
        response.setUserId(certificate.getUserId());
        response.setUserName(user != null ? user.getName() : null);
        response.setUserEmail(user != null ? user.getEmail() : null);
        response.setCourseId(certificate.getCourseId());
        response.setCourseTitle(course != null ? course.getTitle() : null);
        response.setIssuedAt(certificate.getIssuedAt());
        response.setVerificationUrl(certificate.getVerificationUrl());
        response.setGrade(certificate.getGrade());
        response.setDownloadCount(certificate.getDownloadCount());

        return response;
    }
}
