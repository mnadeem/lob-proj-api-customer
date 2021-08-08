package com.org.lob.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hierynomus.mssmb2.SMB2Dialect;
import com.hierynomus.security.bc.BCSecurityProvider;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;

@Configuration
public class SambaClientConfig {

	@Bean
	SMBClient newSambaClient() {
		SmbConfig config = SmbConfig.builder()
				.withSecurityProvider(new BCSecurityProvider())
				.withTimeout(60, TimeUnit.SECONDS) // Timeout sets Read, Write, and Transact timeouts (default is 60 seconds)
				.withSoTimeout(180, TimeUnit.SECONDS) // Socket Timeout (default is 0 seconds, blocks forever)
				.withDialects(SMB2Dialect.SMB_3_0)
				.build();

		return new SMBClient(config);
	}
}
