package com.messenger.protocol.response;


import com.messenger.protocol.request.Request;
import com.messenger.protocol.Chat;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ResponseChat extends Response {

	private static final long serialVersionUID = 1L;

	@Element
	private Chat chat;

	private ResponseChat() {}

	public ResponseChat(byte id, Chat chat)  {
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
