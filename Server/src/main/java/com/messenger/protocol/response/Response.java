package com.messenger.protocol.response;

import com.messenger.protocol.request.Request;
import com.messenger.xml.Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlTransient
public abstract class Response extends Xml implements Responser, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	byte id;
	@XmlAttribute
	byte request;
	@XmlAttribute
	String errorMsg;

	protected Response() {
		this.id = RES_ERROR;
		this.request = Request.REQ_INVALID;
	}

	public Response(byte req, byte id) {
		this.request = req;
		this.id = id;
	}

	public Response(byte req, byte id, String errorMsg){
		this(req, id);
		this.errorMsg = errorMsg;
	}

	@XmlTransient
	public byte getId() {
		return id;
	}

	@XmlTransient
	public byte getRequestId() {
		return request;
	}

	@XmlTransient
	public String getErrorMsg() {
		return errorMsg;
	}
}
