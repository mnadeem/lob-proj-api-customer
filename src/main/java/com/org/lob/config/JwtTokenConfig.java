package com.org.lob.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.org.lob.support.security.jwt.DefaultJwtTokenService;
import com.org.lob.support.security.jwt.JwtTokenFilter;
import com.org.lob.support.security.jwt.JwtTokenService;

@Configuration
public class JwtTokenConfig {

	@Bean
	JwtTokenService jwtTokenService(@Value("${app.jwt.secret}") String jwtSecret, @Value("${app.jwt.token_duration.minutes}") long tokenDurationInMinutes) {
		return new DefaultJwtTokenService(jwtSecret, tokenDurationInMinutes);
	}

	@Bean
	JwtTokenFilter jwtTokenFilter(JwtTokenService jwtTokenService) {
		return new JwtTokenFilter(jwtTokenService);
	}
}
