package de.htwberlin.paymentService.port.product.user.advice;

import de.htwberlin.paymentService.port.product.user.exception.NoPaymentsWithOrderIdFoundException;
import de.htwberlin.paymentService.port.product.user.exception.PaymentIdNotFoundException;
import de.htwberlin.paymentService.port.product.user.exception.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PaymentNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(value = PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String paymentNotFoundHandler(PaymentNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(value = NoPaymentsWithOrderIdFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String paymentWithOrderIdNotFoundHandler(NoPaymentsWithOrderIdFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(value = PaymentIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String paymentIdNotFoundHandler(PaymentIdNotFoundException ex){
        return ex.getMessage();
    }



}
