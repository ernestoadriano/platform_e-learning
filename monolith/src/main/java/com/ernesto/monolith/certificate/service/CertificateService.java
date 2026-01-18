package com.ernesto.monolith.certificate.service;

import com.ernesto.monolith.certificate.model.Certificate;
import com.ernesto.monolith.certificate.repository.CertificateRepository;
import com.ernesto.monolith.common.dto.NotificationType;
import com.ernesto.monolith.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository repository;

    @Autowired
    private NotificationService notificationService;


    public void generate(Long userId, Long courseId) {
        boolean alreadyExists = repository.existsByUserIdAndCourseId(userId, courseId);

        if (alreadyExists) {
            throw new RuntimeException("Certificate already exists");
        }


        Certificate certificate = new Certificate();

        certificate.setUserId(userId);
        certificate.setCourseId(courseId);
        certificate.setCertificateUrl("certificates/" + userId + " - " + courseId + ".pdf");
        repository.save(certificate);

        notificationService.notify(
                userId,
                "Certificate",
                "Course is completed with success. Your certificate is ",
                NotificationType.CERTIFICATE
        );
    }
}
