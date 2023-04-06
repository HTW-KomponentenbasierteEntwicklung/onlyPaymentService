package de.htwberlin.paymentService.port.product.user.exception;

import java.util.UUID;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(UUID orderId) {
        super("Product not found with orderId " + orderId);
    }

}
