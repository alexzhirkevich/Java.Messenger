package com.messenger.protocol;


import java.io.Serializable;

public class UserData implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String firstName;
	private String lastName;
	private Boolean online;
	private String avatarUrl;


	public UserData(Integer id, String avatarUrl, String firstName, String lastName, Boolean online) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.online = online;
		this.avatarUrl = avatarUrl;
	}

	public UserData(UserData user) {
		this(user.id,  user.avatarUrl, user.firstName, user.lastName, user.online);
	}

	public Integer getId() {
		return id;
	}



	public String getLastName() {
		return lastName;
	}


	public String getFirstName() {
		return firstName;
	}


	public Boolean isOnline() {
		return online;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

}
