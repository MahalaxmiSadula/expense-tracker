package com.example.expensetracker.model;

public class AuthenticationResponse {

	public AuthenticationResponse(String token) {
		super();
		this.token = token;
	}

	private String token;

	public AuthenticationResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}