package com.mageddo.springsecurity.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationHeaderAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		final Authentication auth = new HeaderUserAuthentication(request.getHeader("Authorization"));
		SecurityContextHolder
			.getContext()
			.setAuthentication(auth)
		;
		filterChain.doFilter(request, response);
	}

}
