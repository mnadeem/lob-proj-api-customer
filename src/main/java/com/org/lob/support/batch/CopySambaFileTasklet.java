package com.org.lob.support.batch;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import com.org.lob.support.repository.SambaFileRepository;

public class CopySambaFileTasklet implements Tasklet, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopySambaFileTasklet.class);

	private String sambaFileName;
	private String localFilePathKey;
	private String localFileBasePath;
	private SambaFileRepository sambaFileRepository;

	public CopySambaFileTasklet(@NonNull SambaFileRepository sambaFileRepository) {
		this.sambaFileRepository = sambaFileRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String localFilePath = localFileBasePath + sambaFileName;
		copyFile(sambaFileName, localFilePath);
		LOGGER.trace("File copied to {}", localFilePath);
		updateStepExecContext(chunkContext, localFilePath);
		
		return RepeatStatus.FINISHED;
	}

	private void copyFile(String fileName, String localFilePath) throws Exception {
		sambaFileRepository.execute((share) -> {
			try {
				sambaFileRepository.copySambaFile(share, fileName, Path.of(localFilePath));
			} catch (Exception e) {
				LOGGER.error("Error Copying file {}", fileName, e);
				throw new UnexpectedJobExecutionException("Error Copying file " + fileName);
			}
		});
	}

	private void updateStepExecContext(ChunkContext chunkContext, String localFilePath) {
		ExecutionContext executionContext =  chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		executionContext.putString(localFilePathKey, localFilePath);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(sambaFileName, "sambaFileName must be specified");
		Assert.hasText(localFilePathKey, "localFilePathKey must be specified");	
		Assert.notNull(localFileBasePath, "localFileBasePath must be specified");		
	}

	public void setSambaFileName(String sambaFileName) {
		this.sambaFileName = sambaFileName;
	}

	public void setLocalFilePathKey(String localFilePathKey) {
		this.localFilePathKey = localFilePathKey;
	}

	public void setLocalFileBasePath(String localFileBasePath) {
		this.localFileBasePath = localFileBasePath;
	}
}
