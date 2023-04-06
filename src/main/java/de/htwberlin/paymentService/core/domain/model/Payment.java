package de.htwberlin.paymentService.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.awt.Color;
import java.util.UUID;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name="payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @NotNull
    private UUID orderId;

    private String username;

    @NotNull
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    public Payment(UUID orderId, String username, BigDecimal amount, PaymentStatus status, PaymentMethod method) {
        this.orderId = orderId;
        this.username = username;
        this.amount = amount;
        this.status = status;
        this.method = method;
    }
}
