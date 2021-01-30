package com.messenger.protocol.response;

import com.google.gson.Gson;
import com.messenger.protocol.request.IRequest;

public class ResponseSendMessage extends Response {

	private static final long serialVersionUID = 1L;

	private int msgId;

	public ResponseSendMessage(int req, int msgId) {
		super(IRequest.REQ_SENDMSG, req);
		this.msgId = msgId;
	}

	public ResponseSendMessage(int req, int msgId, String errorMsg) {
		super(IRequest.REQ_SENDMSG, req, errorMsg);
		this.msgId = msgId;
	}

	public int getMsgId() {
		return msgId;
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return IRequest.REQ_SENDMSG + gson.toJson(this);
	}
}
