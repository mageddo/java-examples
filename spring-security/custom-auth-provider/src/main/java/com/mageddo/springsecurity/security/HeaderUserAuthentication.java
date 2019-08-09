package com.mageddo.springsecurity.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Collections;

public class HeaderUserAuthentication implements Authentication {

	private final String token;
	private String userName;
	private boolean authenticated;

	public HeaderUserAuthentication(String token) {
		this.token = token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getDetails() {
		return userName;
	}

	@Override
	public Object getPrincipal() {
		return userName;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = true;
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public boolean implies(Subject subject) {
		return false;
	}

	public HeaderUserAuthentication setUserName(String userName) {
		this.userName = userName;
		return this;
	}
}
