package com.messenger.protocol.request;

import com.messenger.protocol.User;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestRegister extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private User user;

	@Element
	private String password;

	private RequestRegister()  {
		super(REQ_REGISTER);
	}

	public RequestRegister(@Element(name = "user")User user,@Element(name = "password") String password) {
		super(REQ_REGISTER);
		setUser(user);
		setPassword(password);
	}

	public User getUser() {
		return user != null ? new User(user):null;
	}

	public void setUser(User user) {
		this.user =  user != null ? new User(user):null ;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}