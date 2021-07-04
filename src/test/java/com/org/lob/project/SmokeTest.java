package com.org.lob.project;


import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class SmokeTest {

	@MockBean
	private CachingConnectionFactory cachingConnectionFactory;
	
	@Autowired
    private DataSource dataSource;

	@Test
	void contextLoads() throws Exception {
		assertThat(dataSource).isNotNull();
	}
}
