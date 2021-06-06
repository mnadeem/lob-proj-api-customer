package com.org.lob.support.security;

import java.util.Date;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
	
	User getUser(String token);
    
    String getUsername(String token);
    
    Date getExpirationDate(String token);

    boolean validate(String token);

    String generateToken(UserDetails userDetails);
}
