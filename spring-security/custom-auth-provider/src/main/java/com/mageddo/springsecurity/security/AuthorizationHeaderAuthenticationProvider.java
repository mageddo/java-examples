package com.mageddo.springsecurity.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuthorizationHeaderAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final var headerUserAuthentication = (HeaderUserAuthentication) authentication;
		// here you can check if the user token is valid or exist on the database for example
		// and set it as authenticated or not
		final User user = findUserByToken(
			String.valueOf(headerUserAuthentication.getCredentials())
		);
		headerUserAuthentication.setUser(user);
		headerUserAuthentication.setAuthenticated(true);
		return headerUserAuthentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		 return HeaderUserAuthentication.class.isAssignableFrom(authentication);
	}

	public Map<String, User> authTokens(){
		return Map.of(
			"Bearer e4549fa4-49ca-4fee-845b-9d21e64088d8", createUser("Jonny Cash", "", "ROLE_API_USER")
		);
	}

	private User createUser(String username, String password, String... authorities) {
		return new User(
			username, password,
			true, true, true, true,
			Stream
				.of(authorities)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList())
		);
	}

	public User findUserByToken(String token){
		if(!authTokens().containsKey(token)){
			throw new UsernameNotFoundException("User not found for token:" + token);
		}
		return authTokens().get(token);
	}
}
