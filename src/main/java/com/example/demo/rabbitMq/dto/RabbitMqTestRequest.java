package com.example.demo.rabbitMq.dto;

public record RabbitMqTestRequest(
        String event,
        Object body
) {
}
