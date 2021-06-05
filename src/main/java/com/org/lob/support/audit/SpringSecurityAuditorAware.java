package com.org.lob.support.audit;

import static com.org.lob.support.Constants.SYSTEM_USER_DEFAULT;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;;

public class SpringSecurityAuditorAware implements AuditorAware<String> {
	
	

    @Override
    public Optional<String> getCurrentAuditor() {

//    	 return Optional.ofNullable(SecurityContextHolder.getContext())
//                 .map(SecurityContext::getAuthentication)
//                 .filter(Authentication::isAuthenticated)
//                 .map(Authentication::getPrincipal)
//                 .map(User.class::cast)
//                 .map(User::getUsername);
        return Optional.of(SYSTEM_USER_DEFAULT);
    }
}