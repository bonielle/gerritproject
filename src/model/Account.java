package model;

import org.json.JSONObject;

import utils.Constants;


public class Account {

	private String name;
	
	private String email;
	
	private String username;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setInfoFromJSON(JSONObject user){
		
		if(user.has(Constants.NAME)){
		setName(user.getString(Constants.NAME));
		}
		if(user.has(Constants.EMAIL)){
		setEmail(user.getString(Constants.EMAIL));
		}
		
		if(user.has(Constants.USERNAME)){
		setUsername(user.getString(Constants.USERNAME));
		}
	}
}
