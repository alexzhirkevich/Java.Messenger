package com.messenger.protocol.response;


import com.google.gson.Gson;
import com.messenger.protocol.request.IRequest;
import com.messenger.protocol.request.Request;
import com.messenger.protocol.request.RequestDialog;
import com.messenger.protocol.request.RequestDialogList;
import com.messenger.protocol.request.RequestLogin;
import com.messenger.protocol.request.RequestRegister;
import com.messenger.protocol.request.RequestSendMessage;

import java.io.Serializable;

public abstract class Response implements IResponse, Serializable {

	private static final long serialVersionUID = 1L;

	private final int res;
	private final int req;
	private String errorMsg;


	public Response(int req, int res) {
		this.req = req;
		this.res = res;
		this.errorMsg = "";
	}

	public Response(int req, int response, String errorMsg){
		this(req, response);
		this.errorMsg = errorMsg;
	}

	public int getResponseCode() {
		return res;
	}

	@Override
	public int getReq() {
		return req;
	}

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

	public static Response decode(String data){
		if (data == null)
			return null;
		int req;
		try {
			req = Integer.parseInt(String.valueOf(data.charAt(0)));
		} catch (NumberFormatException e) {
			return null;
		}
		data = data.substring(1);
		Gson gson = new Gson();

		switch (req){
			case IRequest.REQ_REGISTER:
				return gson.fromJson(data, ResponseRegister.class);
			case IRequest.REQ_LOGIN:
				return gson.fromJson(data, ResponseLogin.class);
			case IRequest.REQ_DIALOG:
				return gson.fromJson(data, ResponseDialog.class);
			case IRequest.REQ_DIALOGLIST:
				return gson.fromJson(data, ResponseDialogList.class);
			case IRequest.REQ_SENDMSG:
				return gson.fromJson(data, ResponseSendMessage.class);
			default:
				return null;
		}
	}

}
