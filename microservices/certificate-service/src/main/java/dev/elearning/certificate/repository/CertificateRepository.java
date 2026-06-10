package dev.elearning.certificate.repository;

import dev.elearning.certificate.model.Certificate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findByCertificateCode(String certificateCode);

    List<Certificate> findAllByUserId(Long userId);

    List<Certificate> findAllByUserIdOrderByIssuedAtDesc(Long userId);

    Page<Certificate> findAllByUserId(Long userId, Pageable pageable);

    List<Certificate> findAllByCourseId(Long courseId);

    Boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    Optional<Certificate> findByUserIdAndCourseId(Long userId, Long courseId);

    Long countByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Certificate c SET c.downloadCount = c.downloadCount + 1 WHERE c.id = :id")
    void incrementDownoloadCount(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Certificate c WHERE c.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Certificate c WHERE c.courseId = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);
}
