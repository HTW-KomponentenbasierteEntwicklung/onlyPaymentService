package de.htwberlin.paymentService.core.domain.service.impl;

import de.htwberlin.paymentService.core.domain.model.Payment;

import java.math.BigDecimal;

public class PaymentValidator {

    public static void validate(Payment payment) {
        if (payment == null)
            throw new IllegalArgumentException("Payment cannot be null.");

        if (payment.getPaymentId() == null)
            throw new IllegalArgumentException("Payment ID cannot be null.");

        if (payment.getOrderId() == null)
            throw new IllegalArgumentException("Order ID cannot be null.");

        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Payment amount must be a positive number.");
        /* Mögliche Erweiterung
        if (payment.getMethod() == null) {
            throw new IllegalArgumentException("Payment method cannot be null.");
        }
         */
    }
}
