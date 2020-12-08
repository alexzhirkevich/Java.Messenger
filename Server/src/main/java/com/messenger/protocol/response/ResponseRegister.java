package com.messenger.protocol.response;

import com.messenger.protocol.request.Request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ResponseRegister extends Response {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private int userID;

	private ResponseRegister() {
	}

	public ResponseRegister(byte id, int userID) {
		super(Request.REQ_REGISTER, id);
		setUserID(id);
	}

	public ResponseRegister(byte id, int userID, String errorMsg) {
		super(Request.REQ_REGISTER, id, errorMsg);
		setUserID(id);
	}

	@XmlTransient
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
}
