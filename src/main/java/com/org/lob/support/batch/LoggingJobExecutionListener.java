package com.org.lob.support.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class LoggingJobExecutionListener extends JobExecutionListenerSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingJobExecutionListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("Executing Job {} ", jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("Finished Job {} With Status {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());		
	}
}
