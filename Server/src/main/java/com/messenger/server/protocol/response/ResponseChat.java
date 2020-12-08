package com.messenger.server.protocol.response;


import com.messenger.server.protocol.Chat;
import com.messenger.server.protocol.request.Request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Arrays;

@XmlRootElement
public class ResponseChat extends Response {

	private static final long serialVersionUID = 1L;

	@XmlElement
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

	@XmlTransient
	public Chat getChat() {
		return chat !=null ? new Chat(chat) : null;
	}

	public void setChat(Chat chat) {
		this.chat = chat!=null ? new Chat(chat) : null;
	}
}
