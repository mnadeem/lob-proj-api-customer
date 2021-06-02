package com.org.lob.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.batch-process.dg.exchange}")
	private String exchange;

	@Value("${rabbitmq.batch_process.triggered.queue}")
	private String batchProcessQueue;

	@Value("${rabbitmq.batch-process.triggered.routingkey}")
	private String batchProcessRoutingKey;

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(exchange);
	}

	@Bean(name = "batchProcessTriggered")
	Queue flightReceivedQueue() {
		return new Queue(batchProcessQueue, true);
	}

	@Bean
	Binding flightReceivedBinding(@Qualifier("batchProcessTriggered") Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(batchProcessRoutingKey);
	}
}