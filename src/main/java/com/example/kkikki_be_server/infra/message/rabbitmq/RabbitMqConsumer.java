package com.example.kkikki_be_server.infra.message.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumer {
	private static final Logger log = LoggerFactory.getLogger(RabbitMqConsumer.class);

	public void consume(Object message) {
		log.info("[RabbitMQ] consumed message={}", message);
	}
}