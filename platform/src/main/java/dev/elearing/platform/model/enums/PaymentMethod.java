package dev.elearing.platform.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethod {

    BANK_TRANSFER("BANK_TRANSFER"),
    PAYPAL("PAYPAL");

    private final String method;
}
