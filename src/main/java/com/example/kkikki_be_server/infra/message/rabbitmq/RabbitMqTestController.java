package com.example.kkikki_be_server.infra.message.rabbitmq;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/rabbitmq")
public class RabbitMqTestController {

    private final RabbitMqProducer rabbitMqProducer;

    @PostMapping("/send")
    public AiMessage<Object> send(@Valid @RequestBody RabbitMqTestRequest request) {
        AiMessage<Object> message = AiMessage.of(request.event(), request.body());
        rabbitMqProducer.send(message);
        return message;
    }
}
