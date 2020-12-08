package com.messenger.server.protocol.response;

import com.messenger.server.protocol.request.Request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ResponseLogin extends Response {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private long userId;

	private ResponseLogin() { }

	public ResponseLogin(byte id, int userId) {
		super(Request.REQ_LOGIN, id);
		setUserId(userId);
	}

	public ResponseLogin(byte id, int userId, String errorMsg) {
		super(Request.REQ_LOGIN, id, errorMsg);
		setUserId(userId);
	}

	@XmlTransient
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
