package com.messenger.protocol.response;

import com.messenger.protocol.request.Request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseSendMessage extends Response {

	private static final long serialVersionUID = 1L;

	private ResponseSendMessage() { }

	public ResponseSendMessage(byte id) {
		super(Request.REQ_SENDMSG, id);
	}

	public ResponseSendMessage(byte id, String errorMsg) {
		super(Request.REQ_SENDMSG, id, errorMsg);
	}
}
