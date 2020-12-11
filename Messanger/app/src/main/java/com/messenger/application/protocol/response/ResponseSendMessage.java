package com.messenger.application.protocol.response;

import com.messenger.application.protocol.request.Request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseSendMessage extends Response {

	private static final long serialVersionUID = 1L;

	private ResponseSendMessage() { }

	@Element
	private int msgId;

	public ResponseSendMessage(byte id,@Element(name = "msgId")int msgId) {
		super(Request.REQ_SENDMSG, id);
		setMsgId(msgId);
	}

	public ResponseSendMessage(byte id, @Element(name = "msgId")int msgId, String errorMsg) {
		super(Request.REQ_SENDMSG, id, errorMsg);
		setMsgId(msgId);
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
}
