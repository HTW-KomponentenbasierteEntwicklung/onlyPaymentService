package de.htwberlin.paymentService.unitTests;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.service.impl.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class PaymentValidatorTests {

    @Test
    public void testValidPayment() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(10.0));
        assertDoesNotThrow(() -> PaymentValidator.validate(payment));
    }

    @Test
    public void testNullPaymentId() {
        Payment payment = new Payment();
        payment.setPaymentId(null);
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(10.0));
        assertThrows(IllegalArgumentException.class, () -> PaymentValidator.validate(payment));
    }

    @Test
    public void testNullPaymentOrderId() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(null);
        payment.setAmount(BigDecimal.valueOf(10.0));
        assertThrows(IllegalArgumentException.class, () -> PaymentValidator.validate(payment));
    }

    @Test
    public void testNullPaymentAmount() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(null);
        assertThrows(IllegalArgumentException.class, () -> PaymentValidator.validate(payment));
    }

    @Test
    public void testNegativePaymentAmount() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(-10.0));
        assertThrows(IllegalArgumentException.class, () -> PaymentValidator.validate(payment));
    }

    @Test
    public void testZeroPaymentAmount() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.ZERO);
        assertDoesNotThrow(() -> PaymentValidator.validate(payment));
    }

}
