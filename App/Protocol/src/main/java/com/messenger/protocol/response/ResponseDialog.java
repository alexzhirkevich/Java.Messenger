package com.messenger.protocol.response;


import com.google.gson.Gson;
import com.messenger.protocol.DialogData;
import com.messenger.protocol.MessageData;
import com.messenger.protocol.request.IRequest;

public class ResponseDialog extends Response {

	private static final long serialVersionUID = 1L;

	private final MessageData[] dialogData;

	public ResponseDialog(int req, MessageData[] messages)  {
		super(IRequest.REQ_INVALID, req);
		this.dialogData = messages;
	}

	public ResponseDialog(int req, MessageData[] messages, String errorMsg){
		super(IRequest.REQ_INVALID, req, errorMsg);
		this.dialogData = messages;
	}

	public MessageData[] getMessages() {
		return dialogData;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return IRequest.REQ_DIALOG + gson.toJson(this);
	}
}
