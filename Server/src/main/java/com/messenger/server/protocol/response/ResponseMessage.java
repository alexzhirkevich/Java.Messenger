package com.messenger.server.protocol.response;


import com.messenger.server.protocol.request.Request;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseMessage extends Response {

	private static final long serialVersionUID = 1L;

	public ResponseMessage()  {
		super(Request.REQ_INVALID, RES_ERROR);
	}

	public ResponseMessage(byte id)  {
		super(Request.REQ_INVALID, id);
	}

	public ResponseMessage(String errorMsg) {
		super(Request.REQ_INVALID, RES_ERROR, errorMsg);
	}

	public ResponseMessage(byte id, String errorMsg){
		super(Request.REQ_INVALID, id, errorMsg);
	}
}
