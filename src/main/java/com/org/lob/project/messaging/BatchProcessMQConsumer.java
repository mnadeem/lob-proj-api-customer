package com.org.lob.project.messaging;


import static com.org.lob.support.Constants.BATCH_JOB_FILE_NAME;
import static com.org.lob.support.Constants.BATCH_JOB_ID;
import static com.org.lob.support.Constants.BATCH_JOB_LAUNCH_DATE;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.lob.project.messaging.model.BatchProcessEvent;

@Component
public class BatchProcessMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchProcessMQConsumer.class);

	private final ObjectMapper mapper;
	private final JobLauncher jobLauncher;
	private final Job job;

	public BatchProcessMQConsumer(ObjectMapper mapper, JobLauncher jobLauncher, Job job) {
		this.mapper = mapper;
		this.jobLauncher = jobLauncher;
		this.job = job;
	}

	@RabbitListener(queues = "#{'${rabbitmq.batch_process.triggered.queue}'}")
	public void consumeMessage(String message) {
		try {
			LOGGER.trace("Message received: {} ", message);

			JobExecution execution = launchJob(create(message));

			LOGGER.debug("Message processed successfully with status {} ", execution.getExitStatus());
		} catch (Exception e) {
			LOGGER.error("Unnable to process the Message", e);
		}
	}

	private BatchProcessEvent create(String message) throws JsonProcessingException {
		return this.mapper.readValue(message, BatchProcessEvent.class);
	}

	private JobExecution launchJob(BatchProcessEvent batchProcessEvent) throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobParameters params = jobParams(batchProcessEvent);
		return this.jobLauncher.run(this.job, params);
	}

	private JobParameters jobParams(BatchProcessEvent batchProcessEvent) {
		return new JobParametersBuilder()
				.addString(BATCH_JOB_ID, String.valueOf(System.currentTimeMillis()))
				.addString(BATCH_JOB_FILE_NAME, batchProcessEvent.getFilePath())
				.addDate(BATCH_JOB_LAUNCH_DATE, new Date())
				.toJobParameters();
	}
}
