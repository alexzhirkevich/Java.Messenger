package com.messenger.application.protocol.request;

import com.messenger.application.xml.Xml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Transient;


import java.io.Serializable;

@Transient
public abstract class Request extends Xml implements Requester, Serializable {

	private static final long serialVersionUID = 1L;

	@Attribute
	protected byte id;

	protected Request() {
		this(REQ_INVALID);
	}

	public Request(@Attribute(name = "id") byte id){
		this.id = id;
	}

	public byte getId() {
		return id;
	}
}
