package com.messenger.protocol.response;


import com.google.gson.Gson;
import com.messenger.protocol.UserData;
import com.messenger.protocol.request.IRequest;

public class ResponseRegister extends Response {

	private static final long serialVersionUID = 1L;

	private final UserData user;

	public ResponseRegister(int req, UserData user) {
		super(IRequest.REQ_REGISTER, req);
		this.user = user;
	}

	public ResponseRegister(int req, UserData user, String errorMsg) {
		super(IRequest.REQ_REGISTER, req, errorMsg);
		this.user = user;
	}

	public UserData getUser() {
		return new UserData(user);
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return IRequest.REQ_REGISTER + gson.toJson(this);
	}

}
