package com.messenger.application.protocol.response;

import com.messenger.application.protocol.User;
import com.messenger.application.protocol.request.Request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseLogin extends Response {

	private static final long serialVersionUID = 1L;

	@Element(required = false)
	private User user;

	private ResponseLogin() { }

	public ResponseLogin(byte id, @Element(name = "user")User user) {
		super(Request.REQ_LOGIN, id);
		setUser(user);
	}

	public ResponseLogin(byte id, @Element(name = "user") User user, String errorMsg) {
		super(Request.REQ_LOGIN, id, errorMsg);
		setUser(user);
	}

	public User getUser() {
		return user!=null ? new User(user) : null;
	}

	public void setUser(User user) {
		this.user = user!=null ? new User(user) : null;
	}
}
