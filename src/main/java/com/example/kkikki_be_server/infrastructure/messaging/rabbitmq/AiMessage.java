package com.example.kkikki_be_server.infrastructure.messaging.rabbitmq;

import java.time.Instant;
import java.util.UUID;

public record AiMessage<T>(Header hd, T bd) {

    public static <T> AiMessage<T> of(String event, T body) {
        return new AiMessage<>(
                new Header(UUID.randomUUID().toString(), event, Instant.now(), 1),
                body
        );
    }

    public record Header(String messageId, String event, Instant occurredAt, int schemaVersion) {
        public Header(String event) {
            this(UUID.randomUUID().toString(), event, Instant.now(), 1);
        }
    }
}
