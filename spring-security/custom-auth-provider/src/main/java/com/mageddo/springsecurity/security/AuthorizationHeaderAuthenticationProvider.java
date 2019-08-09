package com.mageddo.springsecurity.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Component
public class AuthorizationHeaderAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final var headerUserAuthentication = (HeaderUserAuthentication) authentication;
		// here you can check if the user token is valid or exist on the database for example
		// and set it as authenticated or not
		final String userName = findUserByToken(String.valueOf(headerUserAuthentication.getCredentials()));
		headerUserAuthentication.setUserName(userName);
		headerUserAuthentication.setAuthenticated(true);
		return headerUserAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return HeaderUserAuthentication.class.isAssignableFrom(authentication);
	}

	public Map<String, String> authTokens(){
		return Map.of("Bearer e4549fa4-49ca-4fee-845b-9d21e64088d8", "Jonny Cash");
	}
	public String findUserByToken(String token){
		if(!authTokens().containsKey(token)){
			throw new UsernameNotFoundException("User not found for token:" + token);
		}
		return authTokens().get(token);
	}
}
