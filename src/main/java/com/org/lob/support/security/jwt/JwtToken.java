package com.org.lob.support.security.jwt;

import java.util.Date;

public class JwtToken {
	
	private Date iat;
	private Date expiration;
	private String token;

	public JwtToken(Date iat, Date expiration, String token) {
		this.iat = iat;
		this.expiration = expiration;
		this.token = token;
	}

	public Date getIat() {
		return iat;
	}

	public Date getExpiration() {
		return expiration;
	}

	public String getToken() {
		return token;
	}

	public boolean isExpired() {
		return expiration.before(new Date());
	}

    public static Builder builder() {
        return new Builder();
    }

	public static class Builder {
		private Date iat;
		private Date expiration;
		private String token;
		
		public Builder issuedAt(Date iat) {
			this.iat = iat;
			return this;
		}
		
		public Builder expiration(Date expiration) {
			this.expiration = expiration;
			return this;
		}
		
		public Builder expiration(String token) {
			this.token = token;
			return this;
		}

		public JwtToken build() {
			return new JwtToken(iat, expiration, token);
		}
	}
}
