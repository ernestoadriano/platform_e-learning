package dev.elearing.platform.repository;

import dev.elearing.platform.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    List<Certificate> findAllByUserId(Long userId);

}
