package com.messenger.protocol.request;

import com.google.gson.Gson;
import com.messenger.protocol.UserData;

public class RequestRegister extends Request {

	private static final long serialVersionUID = 1L;

	private final UserData user;
	private final String password;

	public RequestRegister(String token, UserData user, String password) {
		super(REQ_REGISTER,token);
		this.user = user;
		this.password = password;
	}

	public UserData getUser() {
		return user != null ? new UserData(user):null;
	}


	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return REQ_REGISTER + gson.toJson(this);
	}
}