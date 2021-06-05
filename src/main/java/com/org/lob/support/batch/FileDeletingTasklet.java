package com.org.lob.support.batch;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class FileDeletingTasklet implements Tasklet, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileDeletingTasklet.class);

	private Resource resource;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		LOGGER.trace("FileName : {}", resource.getURL());
		File file = resource.getFile();

		if (!resource.isFile()) {
			throw new UnexpectedJobExecutionException("Not a file " + resource.getFilename());
		}
		if (!resource.exists()) {
			throw new UnexpectedJobExecutionException("File does not exists " + resource.getFilename());
		}

		boolean deleted = file.delete();
		if (!deleted) {
			throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
		}

		return RepeatStatus.FINISHED;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(resource, "resource must be specified");
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}	
}
