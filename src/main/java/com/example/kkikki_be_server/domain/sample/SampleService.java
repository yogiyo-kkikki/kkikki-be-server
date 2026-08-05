package com.example.kkikki_be_server.domain.sample;

import com.example.kkikki_be_server.global.config.RabbitMqConfig.RabbitMqEndpoint;
import com.example.kkikki_be_server.global.response.MessageBuilder;
import com.example.kkikki_be_server.global.response.MessagePacket;
import com.example.kkikki_be_server.infrastructure.messaging.rabbitmq.RabbitMqProducer;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
	private static final String TEST_EVENT = "UserCreated";

	private final RabbitMqProducer rabbitMqProducer;
	private final RabbitMqEndpoint rabbitMqEndpoint;

	public SampleService(RabbitMqProducer rabbitMqProducer, RabbitMqEndpoint rabbitMqEndpoint) {
		this.rabbitMqProducer = rabbitMqProducer;
		this.rabbitMqEndpoint = rabbitMqEndpoint;
	}

	public SampleDto getSample() {
		return new SampleDto("sample");
	}

	public SampleQueueEnqueueResponse enqueueTestMessage() {
		MessagePacket message = MessageBuilder.build(
			TEST_EVENT,
			MessageBuilder.dataOf("asdf", 1234, "abcd", 5768)
		);

		rabbitMqProducer.send(message);

		return new SampleQueueEnqueueResponse(
			message.hd().tid(),
			message.hd().event(),
			rabbitMqEndpoint.exchange(),
			rabbitMqEndpoint.routingKey(),
			rabbitMqEndpoint.queue()
		);
	}
}