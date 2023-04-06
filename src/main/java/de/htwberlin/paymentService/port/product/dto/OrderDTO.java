package de.htwberlin.paymentService.port.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Data
public class OrderDTO {

    private UUID orderId;
    // ggf. private UUID customerId;
    // ggf. private String shippingAddress;
    private String username;
    private BigDecimal totalAmount;
    private String email;
    private List<OrderItemDTO> items;
}
