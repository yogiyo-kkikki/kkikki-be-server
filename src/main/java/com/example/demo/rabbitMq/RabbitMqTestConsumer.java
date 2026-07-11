package com.example.demo.rabbitMq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqTestConsumer {

    @RabbitListener(queues = "${app.rabbitmq.request-queue}")
    public void receive(String message) {
        System.out.println("받음: " + message);
    }
}
