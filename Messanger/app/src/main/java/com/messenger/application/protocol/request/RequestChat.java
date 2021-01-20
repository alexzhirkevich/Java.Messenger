package com.messenger.application.protocol.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestChat extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private Integer userId;

	@Element
	private Integer withUserId;

	private RequestChat() {
		super(REQ_CHAT);
	}

	public RequestChat(@Element(name = "userId") Integer userId,@Element(name = "withUserId") Integer withUserId) {
		super(REQ_CHAT);
		setUserId(userId);
		setWithUserId(withUserId);
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userID) {
		this.userId = userID;
	}

	public Integer getWithUserId() {
		return withUserId;
	}
	public void setWithUserId(Integer withUserid) {
		this.withUserId = withUserId;
	}

}

