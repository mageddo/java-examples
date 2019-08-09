package com.mageddo.springsecurity.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class AuthorizationHeaderAuthenticationFilter extends OncePerRequestFilter {

	public Map<String, String> authTokens(){
		return Map.of("Bearer e4549fa4-49ca-4fee-845b-9d21e64088d8", "Jonny Cash");
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		final String authToken = request.getHeader("Authorization");
		final String userName = findUserByToken(authToken);
		final Authentication auth = new HeaderUserAuthentication(userName, authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		filterChain.doFilter(request, response);
	}

	public String findUserByToken(String token){
		if(!authTokens().containsKey(token)){
			throw new UsernameNotFoundException("User not found for token:" + token);
		}
		return authTokens().get(token);
	}

}
