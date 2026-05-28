package dev.elearing.platform.repository;

import dev.elearing.platform.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    Boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
