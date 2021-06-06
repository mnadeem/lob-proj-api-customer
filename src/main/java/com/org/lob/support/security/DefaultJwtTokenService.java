package com.org.lob.support.security;

import static com.org.lob.support.Constants.PASSWORD_FAKE;
import static java.util.Optional.ofNullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


public class DefaultJwtTokenService implements JwtTokenService {

	private static final String CLAIM_ROLES = "roles";
	//private static final String CLAIM_EMAIL = "email";

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.token_duration.minutes}")
	private long tokenDurationInMinutes;

	@Override
	public String getUsername(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	@Override
	public Date getExpirationDate(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	@Override
	public User getUser(String token) {
		Claims claims = getAllClaimsFromToken(token);
		String userName = claims.getSubject();
		//String email = claims.get(CLAIM_EMAIL, String.class);
		@SuppressWarnings("unchecked")
		List<String> roles = (List<String>) claims.get(CLAIM_ROLES);
		return new User(userName, PASSWORD_FAKE, buildAuth(roles));
	}

	private Collection<? extends GrantedAuthority> buildAuth(List<String> roles) {		
		return ofNullable(roles).orElse(Collections.emptyList()).stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(jwtSecret.getBytes())
				.parseClaimsJws(token)
				.getBody();
	}

	@Override
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	// while creating the token -
	// 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
	// 2. Sign the JWT using the HS512 algorithm and secret key.
	// 3. According to JWS Compact
	// Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	// compaction of the JWT to a URL-safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenDurationInMinutes)))
				.signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes())
				.compact();
	}

	@Override
	public boolean validate(String token) {
		return !isTokenExpired(token);
	}

	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDate(token);
		return expiration.before(new Date());
	}
}
