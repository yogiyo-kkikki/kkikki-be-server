package com.example.kkikki_be_server.global.config;

import com.example.kkikki_be_server.infra.message.rabbitmq.RabbitMqDispatcher;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
public class RabbitMqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    private final ConnectionFactory connectionFactory;
    private final RabbitMqDispatcher rabbitMqDispatcher;

    private final String exchangeName;
    private final String queueName;
    private final String routingKey;

    private volatile SimpleMessageListenerContainer listenerContainer;

    public RabbitMqConfig(
        ConnectionFactory connectionFactory,
        RabbitMqDispatcher rabbitMqDispatcher,
        @Value("${app.rabbitmq.exchange:kkikki.exchange}") String exchangeName,
        @Value("${app.rabbitmq.queue:kkikki.queue}") String queueName,
        @Value("${app.rabbitmq.routing-key:kkikki.key}") String routingKey
    ) {
        this.connectionFactory = connectionFactory;
        this.rabbitMqDispatcher = rabbitMqDispatcher;
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    @Bean
    public DirectExchange rabbitExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue rabbitQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding rabbitBinding(DirectExchange rabbitExchange, Queue rabbitQueue) {
        return BindingBuilder.bind(rabbitQueue).to(rabbitExchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitMqEndpoint rabbitMqEndpoint() {
        return new RabbitMqEndpoint(exchangeName, queueName, routingKey);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        log.info("[RabbitMQ] init start: exchange={}, queue={}, routingKey={}", exchangeName, queueName, routingKey);
        process();
    }

    public synchronized void process() {
        if (listenerContainer != null && listenerContainer.isRunning()) {
            return;
        }

        MessageConverter converter = new JacksonJsonMessageConverter();
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);
        container.setMissingQueuesFatal(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setConcurrentConsumers(1);
        container.setMessageListener(message -> {
            Object payload;
            try {
                payload = converter.fromMessage(message);
            } catch (Exception ex) {
                payload = new String(message.getBody(), StandardCharsets.UTF_8);
            }
            rabbitMqDispatcher.dispatch(payload);
        });

        container.start();
        listenerContainer = container;
        log.info("[RabbitMQ] process started: subscribed queue={}", queueName);
    }

    @PreDestroy
    public synchronized void destroy() {
        if (listenerContainer == null) {
            return;
        }
        listenerContainer.stop();
        listenerContainer.destroy();
        listenerContainer = null;
        log.info("[RabbitMQ] destroy complete");
    }

    public record RabbitMqEndpoint(String exchange, String queue, String routingKey) {
    }

}