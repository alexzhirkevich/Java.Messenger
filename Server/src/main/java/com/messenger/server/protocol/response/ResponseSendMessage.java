package com.messenger.server.protocol.response;

import com.messenger.server.protocol.request.Request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
