package de.htwberlin.paymentService.port.product.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("paymentToEmail")
    private String paymentToEmailQueue;

    @Value("payment_exchange")
    private String exchange;

    @Value("email_payment_confirmation")
    private String routingKey;

    @Bean
    public Queue queue(){
        return new Queue(paymentToEmailQueue);
    }


    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
    }




}
