package com.ernesto.monolith.order.repository;

import com.ernesto.monolith.order.model.Purchase;
import com.ernesto.monolith.order.model.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    boolean existsByStudentIdAndCourseIdAndStatus(Long studentId, Long courseId, PaymentStatus status);
}
