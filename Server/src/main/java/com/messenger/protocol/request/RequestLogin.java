package com.messenger.protocol.request;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RequestLogin extends Request {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private String phone;

	@XmlElement
	private String passHash;

	private RequestLogin() {
		super(REQ_LOGIN);
	}

	public RequestLogin(String phone, String passHash) {
		super(REQ_LOGIN);
		setPhone(phone);
		setPassHash(passHash);
	}

	@XmlTransient
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@XmlTransient
	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}
}
