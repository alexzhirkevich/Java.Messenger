package com.messenger.server.protocol.request;


import com.messenger.server.protocol.Message;
import com.messenger.server.protocol.User;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RequestMessage extends Request {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private Message message;

	public RequestMessage() {
		super(REQ_MESSAGE);
	}

	public RequestMessage(Message message) {
		super(REQ_MESSAGE);
		setMessage(message);
	}

	@XmlTransient
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = new Message(message);
	}
}

