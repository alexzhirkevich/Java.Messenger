package com.assistant.protocol.response;


import com.messenger.protocol.Xml;
import com.messenger.protocol.request.Request;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Transient;

import java.io.Serializable;

@Transient
public abstract class Response extends Xml implements Responser, Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute
	byte id;

	@Attribute
	byte request;

	@Attribute(required = false)
	String errorMsg;

	protected Response() {
		this.id = RES_ERROR;
		this.request = Request.REQ_INVALID;
	}

	public Response(@Attribute(name = "com/messenger/protocol/request")byte req, @Attribute(name = "id")byte id) {
		this.request = req;
		this.id = id;
	}

	public Response(byte req, byte id, String errorMsg){
		this(req, id);
		this.errorMsg = errorMsg;
	}

	public boolean error(){
		return id != RES_OK;
	}

	public byte getId() {
		return id;
	}

	public byte getRequestId() {
		return request;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
}
