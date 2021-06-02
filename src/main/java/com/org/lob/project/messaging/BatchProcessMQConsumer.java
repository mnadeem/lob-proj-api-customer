package com.org.lob.project.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class BatchProcessMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchProcessMQConsumer.class);

	@Autowired
	private ObjectMapper mapper;

	@RabbitListener(queues = "#{'${rabbitmq.batch_process.triggered.queue}'}")
	public void consumeMessage(String message) {
		try {
			LOGGER.trace("Message received: {} ", message);

			LOGGER.debug("Message processed successfully");
		} catch (Exception e) {
			LOGGER.error("Unnable to process the Message", e);
		}
	}

	private Object create(String message) throws JsonProcessingException {
		//return mapper.readValue(flightMessage, Flight.class);
		return null;
	}
}
