package com.org.lob.support.audit;

import static com.org.lob.support.Constants.SYSTEM_USER_DEFAULT;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

    	Optional<String> secured =  Optional.ofNullable(SecurityContextHolder.getContext())
                 .map(SecurityContext::getAuthentication)
                 .filter(Authentication::isAuthenticated)
                 .map(Authentication::getPrincipal)
                 .map(User.class::cast)
                 .map(User::getUsername);

    	return !secured.isPresent() ? Optional.of(SYSTEM_USER_DEFAULT) : secured;
    }
}