package de.htwberlin.paymentService.port.product.user.exception;

import java.util.UUID;

public class PaymentIdAlreadyExistsException extends IllegalArgumentException {
    public PaymentIdAlreadyExistsException(UUID paymentId) {
        super("Payment ID already exists: " + paymentId);
    }
}
