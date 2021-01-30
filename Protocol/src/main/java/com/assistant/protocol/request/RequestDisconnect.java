package com.assistant.protocol.request;

import com.messenger.protocol.User;

import org.simpleframework.xml.Root;

@Root
public class RequestDisconnect extends Request {

	private static final long serialVersionUID = 1L;

	public RequestDisconnect() {
		super(REQ_DISCONNECT);
	}

}