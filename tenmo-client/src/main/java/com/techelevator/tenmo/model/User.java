package com.techelevator.tenmo.model;

public class User {

	// I think that the user_id here should be a "Long" instead of an "Integer." I, Josh, changed it.
	private Long id;
	private String username;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
