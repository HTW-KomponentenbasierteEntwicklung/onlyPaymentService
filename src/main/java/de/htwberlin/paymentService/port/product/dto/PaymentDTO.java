package de.htwberlin.paymentService.port.product.dto;

import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDTO {
    private UUID paymentId;
    private String username;
    private BigDecimal amount;
    private UUID orderId;
    private PaymentStatus status;
}