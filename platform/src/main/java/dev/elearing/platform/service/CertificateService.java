package dev.elearing.platform.service;

import dev.elearing.platform.dto.CertificateResponse;
import dev.elearing.platform.model.Certificate;
import dev.elearing.platform.model.Course;
import dev.elearing.platform.model.User;
import dev.elearing.platform.repository.CertificateRepository;
import dev.elearing.platform.repository.CourseRepository;
import dev.elearing.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProgressService progressService;

    @Transactional
    public CertificateResponse generateCertificate(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (certificateRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new RuntimeException("Certificate already exists for this course");
        }

        var progress = progressService.getCourseProgress(userId, courseId);

        if (progress.getPercentage() < 100) {
            throw new RuntimeException("Complete all lessons to get certificate. Progress: " + progress.getPercentage() + "%");
        }

        Certificate certificate = new Certificate();
        certificate.setUser(user);
        certificate.setCourse(course);
        certificate.setCertificateCode(generateCertificateCode());
        certificate.setCompletionDate(LocalDateTime.now());
        certificate.setCertificateUrl("/certificates/" + UUID.randomUUID() + ".pdf");

        certificate = certificateRepository.save(certificate);
        return toResponse(certificate);
    }

    public List<CertificateResponse> getUserCertificates(Long userId) {
        return certificateRepository.findAllByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean hasCertificate(Long userId, Long courseId) {
        return certificateRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    private String generateCertificateCode() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private CertificateResponse toResponse(Certificate certificate) {
        CertificateResponse response = new CertificateResponse();
        response.setId(certificate.getId());
        response.setCourseTitle(certificate.getCourse().getTitle());
        response.setUserName(certificate.getUser().getName());
        response.setCompletionDate(certificate.getCompletionDate());
        response.setCertificateCode(certificate.getCertificateCode());
        response.setCertificateUrl(certificate.getCertificateUrl());
        return response;
    }
}
