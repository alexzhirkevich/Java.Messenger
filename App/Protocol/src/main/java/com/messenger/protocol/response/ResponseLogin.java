package com.messenger.protocol.response;

import com.google.gson.Gson;
import com.messenger.protocol.UserData;
import com.messenger.protocol.request.IRequest;

public class ResponseLogin extends ResponseRegister {

	private static final long serialVersionUID = 1L;

	private String token;

	public ResponseLogin(int req, UserData user, String token) {
		super(req, user);
		this.token = token;
	}

	public ResponseLogin(int req, UserData user, String token,String errorMsg) {
		super(req, user, errorMsg);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return IRequest.REQ_LOGIN + gson.toJson(this);
	}

}
