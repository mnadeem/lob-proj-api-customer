package com.org.lob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.org.lob.support.SpringSecurityAuditorAware;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackages = {"com.org.lob.project.repository"})
@EnableTransactionManagement
public class RepositoryConfig {

	@Bean
	AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}
}