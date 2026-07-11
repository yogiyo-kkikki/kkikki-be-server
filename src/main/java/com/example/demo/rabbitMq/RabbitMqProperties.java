package com.example.demo.rabbitMq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitMqProperties(
        String exchange,
        String requestQueue,
        String requestRoutingKey
) {
}
