package de.milchreis.phobox.core.model;

public class UserCredentials {

	private String username;
	private String password;
	
	public UserCredentials() {
	}
	
	public UserCredentials(String username, String password) {
		setUsername(username);
		setPassword(password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
