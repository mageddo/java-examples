package com.mageddo.okhttp.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contributor {

	private String login;
	private int contributions;

	public String getLogin() {
		return login;
	}

	public Contributor setLogin(String login) {
		this.login = login;
		return this;
	}

	public int getContributions() {
		return contributions;
	}

	public Contributor setContributions(int contributions) {
		this.contributions = contributions;
		return this;
	}
}
