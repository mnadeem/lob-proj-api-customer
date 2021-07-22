package com.org.lob.support.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.org.lob.project.batch.model.ContextInfo;

public class CopyFileTasklet implements Tasklet, InitializingBean  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileTasklet.class);

	private String fileName;
	private String localFilePathKey;
	private String localFileBasePath;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String localFilePath = localFileBasePath + fileName;
		copyFile(fileName, localFilePath);
		LOGGER.trace("File copied to {}", localFilePath);
		
		ExecutionContext executionContext =  getExecutionContext(chunkContext);
		updateStepExecContext(executionContext, localFilePath);
		
		return RepeatStatus.FINISHED;
	}

	private void copyFile(String fileName2, String localFilePath) {
		// TODO Auto-generated method stub		
	}

	private void updateStepExecContext(ExecutionContext executionContext, String localFilePath) {
		executionContext.putString(localFilePathKey, localFilePath);
		ContextInfo value = new ContextInfo();
		value.setData(String.valueOf(System.currentTimeMillis()));
		executionContext.put("key", value);
	}

	private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(fileName, "fileName must be specified");
		Assert.hasText(localFilePathKey, "localFilePathKey must be specified");	
		Assert.notNull(localFileBasePath, "localFileBasePath must be specified");	
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocalFilePathKey() {
		return localFilePathKey;
	}

	public void setLocalFilePathKey(String localFilePathKey) {
		this.localFilePathKey = localFilePathKey;
	}

	public String getLocalFileBasePath() {
		return localFileBasePath;
	}

	public void setLocalFileBasePath(String localFileBasePath) {
		this.localFileBasePath = localFileBasePath;
	}
}
