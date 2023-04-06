package de.htwberlin.paymentService.port.product.dto;

import lombok.Data;
import org.springframework.core.SpringVersion;

import java.util.UUID;

@Data
public class OrderItemDTO {

    private UUID productId;
    private int quantity;
}
