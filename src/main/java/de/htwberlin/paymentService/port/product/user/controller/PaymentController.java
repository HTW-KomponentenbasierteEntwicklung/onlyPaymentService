package de.htwberlin.paymentService.port.product.user.controller;

import de.htwberlin.paymentService.core.domain.model.Payment;
import de.htwberlin.paymentService.core.domain.model.PaymentStatus;
import de.htwberlin.paymentService.core.domain.service.interfaces.IPaymentService;
import de.htwberlin.paymentService.port.product.user.exception.*;
import de.htwberlin.paymentService.port.product.user.producer.PaymentProducer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    private PaymentProducer paymentProducer;    //Todo: auch autowired annotation?

    @PostMapping(path = "/payment")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Payment create(@RequestBody Payment payment) throws PaymentIdAlreadyExistsException {
        return paymentService.createPayment(payment);
    }

    @GetMapping("/payment/{orderID}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Payment> getPaymentsByOrderId(@PathVariable UUID orderId) throws NoPaymentsWithOrderIdFoundException {
        return paymentService.getPaymentsByOrderId(orderId);
    }

    @PutMapping(path="/paymentStatus/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Payment updatePaymentStatus (@PathVariable("paymentId") UUID paymentId, @RequestBody PaymentStatus newStatus) throws PaymentIdNotFoundException {
        return paymentService.updatePaymentStatus(paymentId, newStatus);
    }
}
