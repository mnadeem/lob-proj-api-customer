package com.org.lob.config;

import java.net.MalformedURLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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

import com.org.lob.project.repository.entity.Customer;
import com.org.lob.project.service.CustomerService;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

	private JobBuilderFactory jobBuilderFactory;	
	private StepBuilderFactory stepBuilderFactory;	

	public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public BatchConfigurer batchConfigurer(DataSource dataSource) {
		return new DefaultBatchConfigurer(dataSource);
	}

	@Bean
	Job processJob(Step step1, @Value("${app.batch_process.job.name}") String jobName) {
		return jobBuilderFactory.get(jobName)
				.incrementer(new RunIdIncrementer())
				.flow(step1)
				.end()
				.build();
	}

	@Bean
	Step step1(StaxEventItemReader<Customer> reader, ItemWriterAdapter<Customer> writer) {
		return stepBuilderFactory.get("batch-process.step1")
				.<Customer, Customer>chunk(10)
					.reader(reader)
					//.processor(new Processor())
					.faultTolerant()
					//.skipPolicy(skip())
					.writer(writer)
				.build();
	}

	@Bean
	@StepScope
	StaxEventItemReader<Customer> reader(@Value("#{stepExecutionContext['fileName']}") String file)
			throws MalformedURLException {

		LOGGER.info("StaxEventItemReader ----fileName---> {}", file);

		StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
		reader.setResource(new UrlResource(file));
		reader.setFragmentRootElementNames(new String[] { "customer"});
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setClassesToBeBound(Customer.class);
		reader.setUnmarshaller(marshaller);
		return reader;
	}

	@Bean
	ItemWriterAdapter<Customer> customerItemWriter(CustomerService customerService) {
		ItemWriterAdapter<Customer> writer = new ItemWriterAdapter<>();
		writer.setTargetObject(customerService);
		writer.setTargetMethod("create");
		return writer;
	}
}
