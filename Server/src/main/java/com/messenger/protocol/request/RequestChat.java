package com.messenger.protocol.request;


import com.messenger.protocol.User;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestChat extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private User requester;

	@Element
	private User withUser;

	private RequestChat() {
		super(REQ_CHAT);
	}

	public RequestChat(User requester, User withUser) {
		super(REQ_CHAT);
		setRequester(requester);
		setWithUser(withUser);
	}

	public User getRequester() {
		return requester != null ? new User(requester) : null;
	}

	public void setRequester(User user) {
		this.requester = user!=null ? new User(user) : null;
	}

	public User getWithUser() {
		return withUser = withUser !=null ? new User(withUser):null;
	}

	public void setWithUser(User withUser) {
		this.withUser = withUser !=null ? new User(withUser):null;
	}

}

