package com.messenger.application.protocol.request;

import com.messenger.application.protocol.User;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestDisconnect extends Request {

	private static final long serialVersionUID = 1L;

	public RequestDisconnect() {
		super(REQ_DISCONNECT);
	}

}