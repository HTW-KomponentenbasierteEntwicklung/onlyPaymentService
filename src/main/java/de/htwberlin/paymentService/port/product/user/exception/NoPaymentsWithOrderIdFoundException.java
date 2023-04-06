package de.htwberlin.paymentService.port.product.user.exception;

import java.util.UUID;

public class NoPaymentsWithOrderIdFoundException extends IllegalArgumentException {

    public NoPaymentsWithOrderIdFoundException(UUID orderId) {
        super("No payments found with order id: " + orderId);
    }
}
