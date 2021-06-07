package com.org.lob.support.security.jwt;

import java.util.Date;

import org.springframework.security.core.userdetails.User;

public interface JwtTokenService {
	
	User getUser(String token);
    
    String getUsername(String token);
    
    Date getExpirationDate(String token);

    boolean validate(String token);

    JwtToken generateToken(String user);
}
