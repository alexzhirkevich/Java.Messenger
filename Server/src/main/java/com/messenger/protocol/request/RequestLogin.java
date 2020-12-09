package com.messenger.protocol.request;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestLogin extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private String phone;

	@Element
	private String passHash;

	private RequestLogin() {
		super(REQ_LOGIN);
	}

	public RequestLogin(String phone, String passHash) {
		super(REQ_LOGIN);
		setPhone(phone);
		setPassHash(passHash);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
}
