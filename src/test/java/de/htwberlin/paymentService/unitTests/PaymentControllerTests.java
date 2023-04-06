package de.htwberlin.paymentService.unitTests;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.port.product.user.exception.NoPaymentsWithOrderIdFoundException;
import de.htwberlin.paymentService.port.product.user.exception.PaymentIdAlreadyExistsException;
import de.htwberlin.paymentService.port.product.user.exception.PaymentIdNotFoundException;
import de.htwberlin.paymentService.core.domain.service.interfaces.IPaymentService;
import de.htwberlin.paymentService.port.product.user.controller.PaymentController;
import de.htwberlin.paymentService.port.product.user.producer.PaymentProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentControllerTests {

    @Mock
    private IPaymentService paymentService;

    @Mock
    private PaymentProducer paymentProducer;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    public void testCreatePayment() {
        Payment payment = new Payment();
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.TEN);

        when(paymentService.createPayment(payment)).thenReturn(payment);

        Payment paymentCreated = paymentController.create(payment);

        assertNotNull(paymentCreated);
        assertEquals(payment.getPaymentId(), paymentCreated.getPaymentId());
        assertEquals(payment.getAmount(), paymentCreated.getAmount());
    }

    @Test
    public void testCreatePaymentWithInvalidRequest() {
        Payment payment = null;
        when(paymentService.createPayment(payment)).thenThrow(new IllegalArgumentException("Payment cannot be null."));
        assertThrows(IllegalArgumentException.class, () -> paymentController.create(payment));
    }

    @Test
    public void testGetPaymentsByOrderId() {
        UUID orderId = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        List<Payment> payments = Arrays.asList(payment);

        when(paymentService.getPaymentsByOrderId(orderId)).thenReturn(payments);

        List<Payment> paymentsRespond = paymentController.getPaymentsByOrderId(orderId);

        assertEquals(payments, paymentsRespond);
    }

    @Test
    public void testGetPaymentsByOrderIdWithInvalidOrderId() {
        UUID orderId = null;

        when(paymentService.getPaymentsByOrderId(orderId)).thenThrow(new IllegalArgumentException("Order ID is invalid."));

        assertThrows(IllegalArgumentException.class, () -> paymentController.getPaymentsByOrderId(orderId));
    }

    @Test
    public void testGetPaymentsByOrderIdWithNoPaymentsFound() {
        UUID orderId = UUID.randomUUID();

        when(paymentService.getPaymentsByOrderId(orderId)).thenThrow(new NoPaymentsWithOrderIdFoundException(orderId));

        assertThrows(IllegalArgumentException.class, () -> paymentController.getPaymentsByOrderId(orderId));
    }

    @Test
    public void testUpdatePaymentStatus() throws Exception {
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);

        when(paymentService.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS)).thenReturn(payment);
        doNothing().when(paymentProducer).sendMessageToEmailService(payment);

        Payment response = paymentController.updatePaymentStatus(paymentId, PaymentStatus.PENDING);

        assertEquals(payment.getStatus(), response.getStatus());
        verify(paymentProducer).sendMessageToEmailService(payment);
    }

    @Test
    public void testUpdatePaymentStatusWithInvalidPaymentId() {
        UUID paymentId = null;

        when(paymentService.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS)).thenThrow(new IllegalArgumentException("Payment ID is missing or invalid"));

        assertThrows(IllegalArgumentException.class, () -> paymentController.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS));
    }

    @Test
    public void testUpdatePaymentStatusWithPaymentIdNotFound() {
        UUID paymentId = UUID.randomUUID();

        when(paymentService.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS)).thenThrow(new PaymentIdNotFoundException(paymentId));

        assertThrows(PaymentIdNotFoundException.class, () -> paymentController.updatePaymentStatus(paymentId, PaymentStatus.SUCCESS));
    }
}





