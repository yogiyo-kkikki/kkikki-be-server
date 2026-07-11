package com.example.demo.rabbitMq.dto;

public record AiMessage<T>(
        Header hd,
        T bd
) {
    public record Header(String event) {
    }
}
