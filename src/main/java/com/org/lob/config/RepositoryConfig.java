package com.org.lob.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hierynomus.smbj.SMBClient;
import com.org.lob.support.audit.SpringSecurityAuditorAware;
import com.org.lob.support.repository.DefaultSambaFileRepository;
import com.org.lob.support.repository.SambaFileRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaAuditing
@EnableEnversRepositories(basePackages = { "com.org.lob.project.repository" }, entityManagerFactoryRef = "projectEntityManagerFactory", transactionManagerRef = "projectTransactionManager")
@EnableTransactionManagement
public class RepositoryConfig {

	@Primary
	@Bean(name = "projectDataSourceProperties")
	// @ConfigurationProperties("project.datasource")
	DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean(name = "projectDataSource")
	// @ConfigurationProperties("project.datasource.configuration")
	HikariDataSource dataSource(@Qualifier("projectDataSourceProperties") DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Primary
	@Autowired
	@Bean(name = "projectNamedParamJdbcTemplate")
	NamedParameterJdbcTemplate cdrNamedParamJdbcTemplate(@Qualifier("projectDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Primary
	@Bean(name = "projectEntityManagerFactory")
	LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("projectDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.org.lob.project.repository").persistenceUnit("project")
				.build();

	}

	@Primary
	@Bean(name = "projectTransactionManager")
	PlatformTransactionManager transactionManager(
			@Qualifier("projectEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean
	SambaFileRepository sambaFileRepository(SMBClient smbClient) {
		return new DefaultSambaFileRepository(smbClient);
	}

	@Bean
	AuditorAware<String> auditorProvider() {
		return new SpringSecurityAuditorAware();
	}
}