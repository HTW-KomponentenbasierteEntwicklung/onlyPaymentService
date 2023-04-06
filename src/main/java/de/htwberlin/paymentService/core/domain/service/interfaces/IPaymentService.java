package de.htwberlin.paymentService.core.domain.service.interfaces;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.port.product.user.exception.PaymentIdAlreadyExistsException;
import de.htwberlin.paymentService.port.product.user.exception.PaymentIdNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IPaymentService {

    Payment createPayment(Payment payment) throws PaymentIdAlreadyExistsException;

    Payment updatePayment(Payment payment) throws PaymentIdNotFoundException;

    void deletePayment(UUID paymentId);

    Payment updatePaymentStatus(UUID paymentId, PaymentStatus status) throws PaymentIdNotFoundException;

    List<Payment> getPaymentsByOrderId(UUID orderId);

}
