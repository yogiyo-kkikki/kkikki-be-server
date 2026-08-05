package com.example.kkikki_be_server.infra.message.rabbitmq;

import org.springframework.stereotype.Component;

@Component
public class RabbitMqDispatcher {
	private final RabbitMqConsumer rabbitMqConsumer;

	public RabbitMqDispatcher(RabbitMqConsumer rabbitMqConsumer) {
		this.rabbitMqConsumer = rabbitMqConsumer;
	}

	public void dispatch(Object message) {
		rabbitMqConsumer.consume(message);
	}
}