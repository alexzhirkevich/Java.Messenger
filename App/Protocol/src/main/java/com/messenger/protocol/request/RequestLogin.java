package com.messenger.protocol.request;

import com.google.gson.Gson;

public class RequestLogin extends Request {

	private static final long serialVersionUID = 1L;

	private final String phone;
	private final String passHash;

	public RequestLogin(String phone, String passHash) {
		super(REQ_LOGIN,null);
		this.phone = phone;
		this.passHash = passHash;
	}

	public String getPhone() {
		return phone;
	}

	public String getPassHash() {
		return passHash;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return REQ_LOGIN + gson.toJson(this);
	}

}

