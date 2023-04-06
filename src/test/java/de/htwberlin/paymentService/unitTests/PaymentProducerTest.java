package de.htwberlin.paymentService.unitTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.port.product.dto.PaymentDTO;
import de.htwberlin.paymentService.port.product.dtoMapper.PaymentDTOMappingService;
import de.htwberlin.paymentService.port.product.user.producer.PaymentProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class PaymentProducerTest {
    @Mock
    private PaymentDTOMappingService paymentDTOMappingService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentProducer paymentProducer;

    @Test
    public void testSendMessageToEmailService() throws JsonProcessingException {

        Payment payment = new Payment(UUID.randomUUID(), "user123", BigDecimal.valueOf(10.0), PaymentStatus.PENDING, null);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setUsername(payment.getUsername());
        paymentDTO.setAmount(payment.getAmount());

        Mockito.when(paymentDTOMappingService.mapPaymentToPaymentDTO(payment)).thenReturn(paymentDTO);

        paymentProducer.sendMessageToEmailService(payment);

        ObjectMapper objectMapper = new ObjectMapper();
        String expectedMessage = objectMapper.writeValueAsString(paymentDTO);
        verify(rabbitTemplate).convertAndSend("payment_exchange", "email_payment_confirmation", expectedMessage);
    }

    @Test
    public void testSendMessageToEmailServiceWithValidPayment() {

        Payment payment = new Payment();
        payment.setOrderId(UUID.randomUUID());
        payment.setUsername("john");
        payment.setAmount(BigDecimal.valueOf(100.00));
        payment.setStatus(PaymentStatus.PENDING);

        paymentProducer.sendMessageToEmailService(payment);

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), anyString());
    }

    @Test(expected = NullPointerException.class)
    public void testSendMessageToEmailServiceWithNullPayment() {
        paymentProducer.sendMessageToEmailService(null);
    }

    @Test(expected = RuntimeException.class)
    public void testSendMessageToEmailServiceWithInvalidPayment() {
        Payment payment = new Payment();
        payment.setOrderId(UUID.randomUUID());

        paymentProducer.sendMessageToEmailService(payment);
    }

}
