package com.example.kkikki_be_server.batch.scheduler;

import com.example.kkikki_be_server.infrastructure.messaging.rabbitmq.AiMessage;
import com.example.kkikki_be_server.infrastructure.messaging.rabbitmq.RabbitMqProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@EnableScheduling // 중요: 스프링 스케줄링 기능을 활성화합니다. (보통 Application 클래스에 넣기도 하지만 여기 넣어도 무방합니다)
@ConditionalOnProperty(name = "app.ai-analysis.scheduler.enabled", havingValue = "true")
@RequiredArgsConstructor
public class AiAnalysisScheduler {

    private final RabbitMqProducer rabbitMqProducer;

    // 10초마다 실행 (단위: 밀리초, 10000 = 10초)
    // fixedRate: 이전 작업 시작 시간 기준
    // fixedDelay: 이전 작업 종료 시간 기준
    @Scheduled(fixedDelay = 10000)
    public void sendScheduledMessage() {
        log.info("⏰ 스케줄러 작동! RabbitMQ로 메시지를 전송합니다.");

        // 1. 큐에 보낼 데이터 조립 (시간 데이터를 넣어봅시다)
        String currentTime = LocalDateTime.now().toString();
        AiMessage<Object> message = new AiMessage<>(
                new AiMessage.Header("스케줄러_주기적_테스트"),
                "스케줄러가 보낸 시간: " + currentTime
        );

        // 2. 프로듀서를 통해 큐에 쏘기!
        rabbitMqProducer.send(message);
    }
}
