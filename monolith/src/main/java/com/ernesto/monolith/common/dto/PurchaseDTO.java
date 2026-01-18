package com.ernesto.monolith.common.dto;

import com.ernesto.monolith.order.model.enums.PaymentMethod;
import com.ernesto.monolith.order.model.enums.PaymentStatus;


public record PurchaseDTO(
        Long studentId,
        Long courseId,
        Double amount,
        PaymentStatus status,
        PaymentMethod method
) {
}
