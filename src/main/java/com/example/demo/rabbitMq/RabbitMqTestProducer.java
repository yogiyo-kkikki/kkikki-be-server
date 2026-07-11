package com.example.demo.rabbitMq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMqTestProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqProperties rabbitMqProperties;

    public void send(Object message) {
        rabbitTemplate.convertAndSend(
                rabbitMqProperties.exchange(),
                rabbitMqProperties.requestRoutingKey(),
                message
        );
    }
}
