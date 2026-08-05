package com.example.kkikki_be_server.infra.message.rabbitmq;

import jakarta.validation.constraints.NotBlank;

public record RabbitMqTestRequest(
        @NotBlank String event,
        Object body
) {
}
