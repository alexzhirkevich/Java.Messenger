package com.messenger.protocol.request;

import com.google.gson.Gson;

import java.io.Serializable;

public abstract class Request implements IRequest, Serializable {

	private static final long serialVersionUID = 1L;

	protected final int id;
	protected final String token;

	public Request(int id, String token){
		this.token = token;
		this.id = id;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public int getId() {
		return id;
	}

	public static Request decode(String data) {
		if (data == null)
			return null;
		int req;
		try {
			req =Integer.parseInt(String.valueOf(data.charAt(0)));
		} catch (NumberFormatException e) {
			return null;
		}
		data = data.substring(1);
		Gson gson = new Gson();
		switch (req) {
			case REQ_REGISTER:
				return gson.fromJson(data, RequestRegister.class);
			case REQ_LOGIN:
				return gson.fromJson(data, RequestLogin.class);
			case REQ_DIALOG:
				return gson.fromJson(data, RequestDialog.class);
			case REQ_DIALOGLIST:
				return gson.fromJson(data, RequestDialogList.class);
			case REQ_SENDMSG:
				return gson.fromJson(data, RequestSendMessage.class);
			default:
				return null;
		}

	}
}
