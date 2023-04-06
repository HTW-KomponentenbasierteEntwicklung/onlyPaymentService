package de.htwberlin.paymentService.port.product.dtoMapper;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.port.product.dto.*;
import org.springframework.stereotype.Service;

@Service
public class PaymentDTOMappingService {

    public PaymentDTO mapPaymentToPaymentDTO(Payment payment) {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setOrderId(payment.getOrderId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setStatus(payment.getStatus());
        paymentDTO.setUsername(payment.getUsername());

        return paymentDTO;
    }

    public Payment mapPaymentDTOToPayment(PaymentDTO paymentDTO) {

        Payment payment = new Payment();
        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setOrderId(paymentDTO.getOrderId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setStatus(paymentDTO.getStatus());
        payment.setUsername(paymentDTO.getUsername());

        return payment;
    }
}
