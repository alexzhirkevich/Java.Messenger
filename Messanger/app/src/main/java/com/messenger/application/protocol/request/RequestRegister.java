package com.messenger.application.protocol.request;

import com.messenger.application.protocol.User;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestRegister extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private User user;

	@Element
	private String passHash;

	private RequestRegister()  {
		super(REQ_REGISTER);
	}

	public RequestRegister(User user, String passHash) {
		super(REQ_REGISTER);
		setUser(user);
		setPassHash(passHash);
	}

	public User getUser() {
		return user != null ? new User(user):null;
	}

	public void setUser(User user) {
		this.user =  user != null ? new User(user):null ;
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
}
