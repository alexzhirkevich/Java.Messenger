package com.messenger.protocol.request;

import com.messenger.protocol.Message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class RequestSendMessage extends Request {

	private static final long serialVersionUID = 1L;


	@XmlElement
	private Message message;

	private RequestSendMessage() {
		super(REQ_SENDMSG);
	}

	public RequestSendMessage(Message message) {
		super(REQ_SENDMSG);
	}

	@XmlTransient
	public Message getMessage() {
		return message != null ? new Message(message) : null;
	}

	public void setMessage(Message message) {
		this.message = message != null ? new Message(message) : null;
	}
}


