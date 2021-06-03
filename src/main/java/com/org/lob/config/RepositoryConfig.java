package com.org.lob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.org.lob.support.SpringSecurityAuditorAware;

@Configuration
@EnableEnversRepositories
@EnableTransactionManagement
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class, basePackages = {"com.org.lob.project.repository"}) 
@EnableJpaAuditing
public class RepositoryConfig {

	@Bean
	AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}
}