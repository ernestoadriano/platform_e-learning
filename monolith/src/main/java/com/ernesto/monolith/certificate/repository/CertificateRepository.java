package com.ernesto.monolith.certificate.repository;

import com.ernesto.monolith.certificate.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
