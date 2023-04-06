package de.htwberlin.paymentService.unitTests;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentMethod;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.port.product.user.exception.*;
import de.htwberlin.paymentService.core.domain.service.impl.PaymentService;
import de.htwberlin.paymentService.core.domain.service.interfaces.IPaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTests {

    @Mock
    IPaymentRepository mockPaymentRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    public void createValidPaymentTest() {

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setMethod(PaymentMethod.AUF_RECHNUNG);

        when(mockPaymentRepository.existsById(payment.getPaymentId())).thenReturn(false);
        when(mockPaymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.createPayment(payment);
        verify(mockPaymentRepository, times(1)).save(payment);
        assertEquals(payment, savedPayment);
    }

   @Test
    public void createPaymentWithExistingPaymentIdTest() {
        Payment payment = new Payment();
        UUID paymentId = UUID.randomUUID();
        payment.setPaymentId(paymentId);
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));
        payment.setMethod(PaymentMethod.AUF_RECHNUNG);

        when(mockPaymentRepository.existsById(paymentId)).thenReturn(true);
        assertThrows(PaymentIdAlreadyExistsException.class, () -> paymentService.createPayment(payment));
        verify(mockPaymentRepository, never()).save(payment);
    }

    @Test
    public void createPaymentWithNegativeAmountTest() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(-100.0));

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));

        verify(mockPaymentRepository, never()).save(payment);
    }

    @Test
    public void createPaymentWithNullPaymentIdTest() {
        Payment payment = new Payment();
        payment.setPaymentId(null);
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
        verify(mockPaymentRepository, never()).save(payment);
    }

    @Test
    public void createPaymentWithNullOrderIdTest() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(null);
        payment.setAmount(BigDecimal.valueOf(100.0));

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
        verify(mockPaymentRepository, never()).save(payment);
    }

    @Test
    public void createPaymentWithNullAmountTest() {
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(null);

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(payment));
        verify(mockPaymentRepository, never()).save(payment);
    }

    @Test
    public void updatePaymentStatusSuccessTest() {
        UUID paymentId = UUID.randomUUID();
        PaymentStatus newPaymentStatus = PaymentStatus.PENDING;

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(200.0));
        when(mockPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(mockPaymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.updatePaymentStatus(paymentId, newPaymentStatus);

        assertThat(result.getStatus()).isEqualTo(newPaymentStatus);
        verify(mockPaymentRepository, times(1)).findById(paymentId);
        verify(mockPaymentRepository, times(1)).save(payment);
    }

    @Test
    public void updatePaymentStatusPaymentIdNotFoundTest() {
        UUID paymentId = UUID.randomUUID();
        PaymentStatus newPaymentStatus = PaymentStatus.PENDING;
        when(mockPaymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentIdNotFoundException.class, () -> paymentService.updatePaymentStatus(paymentId, newPaymentStatus));
        verify(mockPaymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void updatePaymentStatusWithStatusNullTest() {
        UUID paymentId = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(paymentId, null));
        verify(mockPaymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void updatePaymentStatusWithPaymentIdNullTest() {
        PaymentStatus paymentStatus = PaymentStatus.PENDING;

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentStatus(null, paymentStatus));
    }

    @Test
    public void updatePaymentStatusWithRepositoryExceptionTest() {
        UUID paymentId = UUID.randomUUID();
        PaymentStatus newPaymentStatus = PaymentStatus.PENDING;

        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));

        when(mockPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(mockPaymentRepository.save(any())).thenThrow(new IllegalArgumentException());

        assertThrows(Exception.class, () -> paymentService.updatePaymentStatus(paymentId, newPaymentStatus));
    }

    @Test
    public void updatePaymentStatusOnlyCallsRepositoryOnceTest() {
        UUID paymentId = UUID.randomUUID();
        PaymentStatus newPaymentStatus = PaymentStatus.PENDING;
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));

        when(mockPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(mockPaymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.updatePaymentStatus(paymentId, newPaymentStatus);

        verify(mockPaymentRepository, times(1)).findById(paymentId);
        verify(mockPaymentRepository, times(1)).save(payment);
    }

    @Test
    public void updatePaymentStatus_ReturnsPaymentWithUpdatedStatusTest() throws PaymentIdNotFoundException {
        UUID paymentId = UUID.randomUUID();
        PaymentStatus newPaymentStatus = PaymentStatus.PENDING;
        Payment payment = new Payment();
        payment.setPaymentId(paymentId);
        payment.setUsername("testname");
        payment.setOrderId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100.0));
        when(mockPaymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(mockPaymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.updatePaymentStatus(paymentId, newPaymentStatus);

        assertThat(result.getPaymentId()).isEqualTo(paymentId);
        assertThat(result.getUsername()).isEqualTo("testname");
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(100.0));
        assertThat(result.getStatus()).isEqualTo(newPaymentStatus);
    }


    @Test
    void getPaymentsByOrderIdValidOrderTest() {
        UUID orderId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setPaymentId(paymentId);
        payment.setAmount(BigDecimal.valueOf(10.0));

        when(mockPaymentRepository.findByOrderId(orderId)).thenReturn(Collections.singletonList(payment));

        List<Payment> result = paymentService.getPaymentsByOrderId(orderId);

        assertEquals(1, result.size());
        assertEquals(orderId, result.get(0).getOrderId());
    }

    @Test
    void getPaymentsByOrderIdWithTwoValidOrdersTest() {
        UUID orderId = UUID.randomUUID();
        Payment payment1 = new Payment();
        payment1.setOrderId(orderId);
        payment1.setPaymentId(UUID.randomUUID());
        payment1.setAmount(BigDecimal.valueOf(10.0));
        Payment payment2 = new Payment();
        payment2.setOrderId(orderId);
        payment2.setPaymentId(UUID.randomUUID());
        payment2.setAmount(BigDecimal.valueOf(20.0));
        paymentService.createPayment(payment1);
        paymentService.createPayment(payment2);

        when(mockPaymentRepository.findByOrderId(orderId)).thenReturn(Arrays.asList(payment1, payment2));

        List<Payment> payments = paymentService.getPaymentsByOrderId(orderId);

        assertEquals(2, payments.size());
        assertEquals(orderId, payments.get(0).getOrderId());
        assertEquals(orderId, payments.get(1).getOrderId());
    }

    @Test
    void getPaymentsByOrderIdWithInvalidOrderIdTest () {
        UUID orderId = UUID.randomUUID();
        when(mockPaymentRepository.findByOrderId(orderId)).thenThrow(NoPaymentsWithOrderIdFoundException.class);
        assertThrows(NoPaymentsWithOrderIdFoundException.class, () -> paymentService.getPaymentsByOrderId(orderId));
    }

    @Test
    void getPaymentsByOrderIdWithNullOrderIdTest() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.getPaymentsByOrderId(null));
    }

}
