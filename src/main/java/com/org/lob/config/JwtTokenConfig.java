package com.org.lob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.org.lob.support.security.DefaultJwtTokenService;
import com.org.lob.support.security.JwtTokenFilter;
import com.org.lob.support.security.JwtTokenService;

@Configuration
public class JwtTokenConfig {

	@Bean
	JwtTokenService jwtTokenService() {
		return new DefaultJwtTokenService();
	}

	@Bean
	JwtTokenFilter jwtTokenFilter(JwtTokenService jwtTokenService) {
		return new JwtTokenFilter(jwtTokenService);
	}
}
