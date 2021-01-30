package com.assistant.protocol.request;

import com.messenger.protocol.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class RequestSendMessage extends Request {

	private static final long serialVersionUID = 1L;

	@Element
	private Message message;

	private RequestSendMessage() {
		super(REQ_SENDMSG);
	}

	public RequestSendMessage(@Element(name = "message")Message message) {
		super(REQ_SENDMSG);
	}

	public Message getMessage() {
		return message != null ? new Message(message) : null;
	}

	public void setMessage(Message message) {
		this.message = message != null ? new Message(message) : null;
	}
}


