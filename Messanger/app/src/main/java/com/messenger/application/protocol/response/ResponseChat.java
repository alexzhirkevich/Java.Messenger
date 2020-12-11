package com.messenger.application.protocol.response;


import com.messenger.application.protocol.request.Request;
import com.messenger.application.protocol.Chat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseChat extends Response {

	private static final long serialVersionUID = 1L;

	@Element(required = false)
	private Chat chat;

	private ResponseChat() {}

	public ResponseChat(byte id, @Element(name = "chat")Chat chat)  {
		super(Request.REQ_INVALID, id);
		setChat(chat);
	}

	public ResponseChat(byte id, Chat chat, String errorMsg){
		super(Request.REQ_INVALID, id, errorMsg);
		setChat(chat);
	}

	public Chat getChat() {
		return chat !=null ? new Chat(chat) : null;
	}

	public void setChat(Chat chat) {
		this.chat = chat!=null ? new Chat(chat) : null;
	}
}
