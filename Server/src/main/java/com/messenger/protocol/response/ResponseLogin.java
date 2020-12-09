package com.messenger.protocol.response;

import com.messenger.protocol.request.Request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseLogin extends Response {

	private static final long serialVersionUID = 1L;

	@Element
	private long userId;

	private ResponseLogin() { }

	public ResponseLogin(byte id, int userId) {
		super(Request.REQ_LOGIN, id);
		setUserId(userId);
	}

	public ResponseLogin(byte id, int userId, String errorMsg) {
		super(Request.REQ_LOGIN, id, errorMsg);
		setUserId(userId);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
