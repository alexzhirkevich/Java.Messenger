package com.messenger.protocol.request;

import com.google.gson.Gson;
import com.messenger.protocol.MessageData;

public class RequestSendMessage extends Request {

	private static final long serialVersionUID = 1L;

	private final MessageData messageData;

	public RequestSendMessage(String token, MessageData messageData) {
		super(REQ_SENDMSG,token);
		this.messageData = messageData;
	}

	public MessageData getMessage() {
		return messageData != null ? new MessageData(messageData) : null;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return REQ_SENDMSG + gson.toJson(this);
	}
}


