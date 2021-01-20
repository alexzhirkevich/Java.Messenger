package com.messenger.application.protocol.request;

import com.messenger.application.protocol.User;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestDialogMembers extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private User requester;

	private RequestDialogMembers() {
		super(REQ_DIALOGIDS);
	}

	public RequestDialogMembers(@Element(name = "requester") User requester) {
		super(REQ_DIALOGIDS);
		setRequester(requester);
	}

	public User getRequester() {
		return requester != null ? new User(requester) : null;
	}

	public void setRequester(User user) {
		this.requester = user!=null ? new User(user) : null;
	}


}
