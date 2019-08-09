package com.mageddo.springsecurity.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.security.auth.Subject;
import java.util.Collection;

public class HeaderUserAuthentication implements Authentication {

	private final String token;
	private User user;
	private boolean authenticated;

	public HeaderUserAuthentication(String token) {
		this.token = token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getDetails() {
		return this.user.getUsername();
	}

	@Override
	public Object getPrincipal() {
		return this.user;
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
		return user.getUsername();
	}

	@Override
	public boolean implies(Subject subject) {
		return false;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
