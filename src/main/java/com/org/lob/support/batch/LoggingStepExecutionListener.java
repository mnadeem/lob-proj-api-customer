package com.org.lob.support.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class LoggingStepExecutionListener extends StepExecutionListenerSupport {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingStepExecutionListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Executing Step {}", stepExecution.getStepName());
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("Finished Step {} with staus {}", stepExecution.getStepName(), stepExecution.getExitStatus());
		return stepExecution.getExitStatus();
	}

}
