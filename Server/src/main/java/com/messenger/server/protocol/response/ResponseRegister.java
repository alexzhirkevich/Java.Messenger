package com.messenger.server.protocol.response;

import com.messenger.server.protocol.request.Request;
import com.messenger.server.protocol.request.RequestException;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseRegister extends Response {

	private static final long serialVersionUID = 1L;

	private ResponseRegister() {
	}

	public ResponseRegister(byte id) {
		super(Request.REQ_REGISTER, id);
	}

	public ResponseRegister(byte id, String errorMsg) {
		super(Request.REQ_REGISTER, id, errorMsg);
	}
}
