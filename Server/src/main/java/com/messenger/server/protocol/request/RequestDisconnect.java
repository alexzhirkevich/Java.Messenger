package com.messenger.server.protocol.request;

public class RequestDisconnect extends Request {

	private static final long serialVersionUID = 1L;

	public RequestDisconnect() throws RequestException {
		super(REQ_LOGIN);
	}

}
