package com.messenger.protocol.request;

import com.messenger.protocol.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestDisconnect extends Request {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private User user;

	private RequestDisconnect() throws RequestException {
		super(REQ_DISCONNECT);
	}

	private RequestDisconnect(User user) throws RequestException {
		super(REQ_DISCONNECT);
		setUser(user);
	}

	public void setUser(User user) {
		this.user = user!=null ? new User(user): null;
	}

	public User getUser() {
		return user!=null ? new User(user) : null;
	}
}
