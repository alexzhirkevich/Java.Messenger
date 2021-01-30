package com.assistant.protocol.response;

import com.messenger.protocol.User;
import com.messenger.protocol.request.Request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseRegister extends Response {

	private static final long serialVersionUID = 1L;

	@Element
	private User user;

	private ResponseRegister() {
	}

	public ResponseRegister(byte id, @Element(name = "user")User user) {
		super(Request.REQ_REGISTER, id);
		setUser(user);
	}

	public ResponseRegister(byte id, @Element(name = "user")User user, String errorMsg) {
		super(Request.REQ_REGISTER, id, errorMsg);
		setUser(user);
	}

	public User getUser() {
		return new User(user);
	}

	public void setUser(User user) {
		this.user = new User(user);
	}
}
