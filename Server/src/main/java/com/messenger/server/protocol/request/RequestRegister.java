package com.messenger.server.protocol.request;

import com.messenger.server.protocol.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RequestRegister extends Request {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private User user;

	@XmlElement
	private String passHash;

	private RequestRegister()  {
		super(REQ_REGISTER);
	}

	public RequestRegister(User user, String passHash) {
		super(REQ_REGISTER);
		setUser(user);
		setPassHash(passHash);
	}

	@XmlTransient
	public User getUser() {
		return user != null ? new User(user):null;
	}

	public void setUser(User user) {
		this.user =  user != null ? new User(user):null ;
	}

	@XmlTransient
	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
}
