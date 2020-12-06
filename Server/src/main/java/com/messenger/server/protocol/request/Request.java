package com.messenger.server.protocol.request;

import com.messenger.server.xml.Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@XmlTransient
public abstract class Request extends Xml implements Requester, Serializable {

	private static final long serialVersionUID = 1L;

	@XmlAttribute
	protected byte id;

	protected Request() {
		this(REQ_INVALID);
	}

	public Request(byte id){
		this.id = id;
	}

	@XmlTransient
	public byte getId() {
		return id;
	}
}
