package com.example.demo.rabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqConfig(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    @Bean
    public Queue testQueue() {
        return new Queue(rabbitMqProperties.requestQueue(), true);
    }

    @Bean
    public DirectExchange testExchange() {
        return new DirectExchange(rabbitMqProperties.exchange());
    }

    @Bean
    public Binding testBinding(Queue testQueue, DirectExchange testExchange) {
        return BindingBuilder.bind(testQueue)
                .to(testExchange)
                .with(rabbitMqProperties.requestRoutingKey());
    }
}
