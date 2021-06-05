package com.org.lob.config;

import static com.org.lob.support.Constants.BATCH_JOB_ROOT_ELEMENT;
import static com.org.lob.support.Constants.BATCH_JOB_TARGET_METHOD;

import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.org.lob.project.batch.CustomerProcessor;
import com.org.lob.project.batch.model.CustomerData;
import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.CustomerService;
import com.org.lob.support.batch.LoggingJobExecutionListener;
import com.org.lob.support.batch.LoggingStepExecutionListener;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

	private JobBuilderFactory jobBuilderFactory;	
	private StepBuilderFactory stepBuilderFactory;	

	BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	JobExecutionListener loggingJobExecutionListener() {
		return new LoggingJobExecutionListener();
	}

	@Bean
	StepExecutionListener loggingStepExecutionListener() {
		return new LoggingStepExecutionListener();
	}

	@Bean
	BatchConfigurer batchConfigurer(DataSource dataSource) {
		return new DefaultBatchConfigurer(dataSource);
	}

	@Bean
	Job processJob(Step step1, @Value("${app.batch_process.job.name}") String jobName, JobExecutionListener executionListener) {
		return jobBuilderFactory.get(jobName)
				//.validator(null)
				.incrementer(new RunIdIncrementer())
				.listener(executionListener)
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	Step step1(@Value("${app.batch_process.step1.name}") String stepName, StaxEventItemReader<CustomerData> reader, CustomerProcessor processor, ItemWriterAdapter<Customer> writer, StepExecutionListener stepExecutionListener) {
		return stepBuilderFactory.get(stepName)
				.listener(stepExecutionListener)
				.<CustomerData, Customer>chunk(10)
					.reader(reader)
					.processor(processor)
					.faultTolerant()
					//.skipPolicy(skip())
					.writer(writer)
				.build();
	}

	@Bean
	@StepScope
	StaxEventItemReader<CustomerData> customerDataReader(@Value("#{jobParameters['fileName']}") String file) throws MalformedURLException {

		LOGGER.info("customerDataReader:fileName: {}", file);

		StaxEventItemReader<CustomerData> reader = new StaxEventItemReader<>();
		reader.setResource(new UrlResource(file));
		reader.setFragmentRootElementNames(new String[] { BATCH_JOB_ROOT_ELEMENT });
		reader.setUnmarshaller(newCustomerDataMarshaller());
		return reader;
	}

	private Jaxb2Marshaller newCustomerDataMarshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(CustomerData.class);
		return marshaller;
	}

	@Bean
	CustomerProcessor customerProcessor() {
		return new CustomerProcessor();
	}

	@Bean
	ItemWriterAdapter<Customer> customerItemWriter(CustomerService customerService) {
		ItemWriterAdapter<Customer> writer = new ItemWriterAdapter<>();
		writer.setTargetObject(customerService);
		writer.setTargetMethod(BATCH_JOB_TARGET_METHOD);
		return writer;
	}
}