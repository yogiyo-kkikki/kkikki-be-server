package com.example.demo.rabbitMq;

import com.example.demo.rabbitMq.dto.AiMessage;
import com.example.demo.rabbitMq.dto.RabbitMqTestRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitMqTestController {

    private final RabbitMqTestProducer rabbitMqTestProducer;

    @PostMapping("/send")
    public AiMessage<Object> send(@RequestBody RabbitMqTestRequest request) {
        AiMessage<Object> message = new AiMessage<>(
                new AiMessage.Header(request.event()),
                request.body()
        );

        rabbitMqTestProducer.send(message);
        return message;
    }
}
