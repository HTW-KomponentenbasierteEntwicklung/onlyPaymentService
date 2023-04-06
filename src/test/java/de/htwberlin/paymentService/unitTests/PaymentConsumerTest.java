package de.htwberlin.paymentService.unitTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.core.domain.service.interfaces.IPaymentService;
import de.htwberlin.paymentService.port.product.dto.OrderDTO;
import de.htwberlin.paymentService.port.product.user.consumer.PaymentConsumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentConsumerTest {
    @Mock
    private IPaymentService paymentService;

    @InjectMocks
    private PaymentConsumer paymentConsumer;

    @Test
    public void testConsumeOrder() throws JsonProcessingException {

        OrderDTO orderDTO = new OrderDTO();
        UUID orderId = UUID.randomUUID();
        orderDTO.setOrderId(orderId);
        orderDTO.setUsername("user1");
        orderDTO.setTotalAmount(BigDecimal.valueOf(100.0));

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(orderDTO);

        paymentConsumer.consumeOrder(message);

        Payment expectedPayment = new Payment(orderId, "user1", BigDecimal.valueOf(100.0), PaymentStatus.PENDING, null);
        verify(paymentService).createPayment(expectedPayment);
        assertEquals(orderId, expectedPayment.getOrderId());
        assertEquals("john", expectedPayment.getUsername());
        assertEquals(BigDecimal.valueOf(100.0), expectedPayment.getAmount());
        assertEquals(PaymentStatus.PENDING, expectedPayment.getStatus());
        assertNull(expectedPayment.getMethod());
    }

    @Test
    public void testConsumeOrderInvalidMessage() {
        String message = "invalid message";
        assertThrows(RuntimeException.class, () -> paymentConsumer.consumeOrder(message));
    }

    @Test
    public void testConsumeOrder_ExceptionThrown() throws JsonProcessingException {
        OrderDTO orderDTO = new OrderDTO();
        UUID orderId = UUID.randomUUID();
        orderDTO.setOrderId(orderId);
        orderDTO.setUsername("user2");
        orderDTO.setTotalAmount(BigDecimal.valueOf(200.0));

        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(orderDTO);

        doThrow(new RuntimeException("Error creating payment")).when(paymentService).createPayment(any(Payment.class));

        assertThrows(RuntimeException.class, () -> paymentConsumer.consumeOrder(message));
    }

}
