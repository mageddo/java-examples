package com.mageddo.springsecurity;

import com.mageddo.springsecurity.security.AuthorizationHeaderAuthenticationFilter;
import com.mageddo.springsecurity.security.AuthorizationHeaderAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthorizationHeaderAuthenticationProvider authorizationHeaderAuthenticationProvider;
	private final AuthorizationHeaderAuthenticationFilter authorizationHeaderAuthenticationFilter;

	public SecurityConfig(AuthorizationHeaderAuthenticationProvider authorizationHeaderAuthenticationProvider, AuthorizationHeaderAuthenticationFilter authorizationHeaderAuthenticationFilter) {
		this.authorizationHeaderAuthenticationProvider = authorizationHeaderAuthenticationProvider;
		this.authorizationHeaderAuthenticationFilter = authorizationHeaderAuthenticationFilter;
	}

	public void configure(final HttpSecurity http) throws Exception {
		http
			.authenticationProvider(authorizationHeaderAuthenticationProvider)
			.antMatcher("/api/**")
				.addFilterBefore(authorizationHeaderAuthenticationFilter, BasicAuthenticationFilter.class)
			.authorizeRequests()
			.anyRequest()
			.hasAnyRole("API_USER")
//			.authenticated()
		;
	}

}
