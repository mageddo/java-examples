package com.mageddo.springsecurity.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHeaderAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final var headerUserAuthentication = (HeaderUserAuthentication) authentication;
		// here you can check if the user token is valid or exist on the database for example
		// and set it as authenticated or not
		headerUserAuthentication.setAuthenticated(true);
		return headerUserAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return HeaderUserAuthentication.class.isAssignableFrom(authentication);
	}
}
