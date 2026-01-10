package com.ernesto.monolith.certificate.service;

import com.ernesto.monolith.certificate.model.Certificate;
import com.ernesto.monolith.certificate.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository repository;

    public void generate(Long userId, Long courseId) {
        if (repository.existsByUserIdAndCourseId(userId, courseId)) {
            return;
        }

        Certificate certificate = new Certificate();

        certificate.setUserId(userId);
        certificate.setCourseId(courseId);
        repository.save(certificate);
    }
}
