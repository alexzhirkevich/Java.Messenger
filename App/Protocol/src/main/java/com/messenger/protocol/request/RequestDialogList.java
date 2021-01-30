package com.messenger.protocol.request;


import com.google.gson.Gson;

public class RequestDialogList extends Request {

	private static final long serialVersionUID = 1L;

	public RequestDialogList(String token) {
		super(REQ_DIALOGLIST,token);
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return REQ_DIALOGLIST + gson.toJson(this);
	}

}
