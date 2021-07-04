package com.org.lob.project;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class SmokeTest {

	@MockBean
	private CachingConnectionFactory cachingConnectionFactory;


	@Test
	public void contextLoads() throws Exception {

	}
}
