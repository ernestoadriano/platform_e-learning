package dev.elearing.platform.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PurchaseStatus {

    PENDIND("PENDING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED");

    private final String status;
}
