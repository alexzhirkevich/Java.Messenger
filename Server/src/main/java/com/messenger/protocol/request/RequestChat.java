package com.messenger.protocol.request;


import com.messenger.protocol.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RequestChat extends Request {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private User requester;

	@XmlElement
	private User withUser;

	private RequestChat() {
		super(REQ_CHAT);
	}

	public RequestChat(User requester, User withUser) {
		super(REQ_CHAT);
		setRequester(requester);
		setWithUser(withUser);
	}

	@XmlTransient
	public User getRequester() {
		return requester != null ? new User(requester) : null;
	}

	public void setRequester(User user) {
		this.requester = user!=null ? new User(user) : null;
	}

	@XmlTransient
	public User getWithUser() {
		return withUser = withUser !=null ? new User(withUser):null;
	}

	public void setWithUser(User withUser) {
		this.withUser = withUser !=null ? new User(withUser):null;
	}

}

