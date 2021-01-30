package com.messenger.protocol;

import java.io.Serializable;
import java.util.Date;


public class MessageData implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Integer id;
	private final Integer dialogId;
	private final UserData from;
	private final MessageContent content;
	private final Date date;

	public MessageData(MessageData msg){
		this(msg.getId(),msg.getDialogId(),msg.getFrom(), msg.getContent(), msg.getDate());
	}

	public MessageData(Integer id, Integer dialogId, UserData from, MessageContent content, Date date){
		this.id = id;
		this.dialogId = dialogId;
		this.from = from;
		this.content = content;
		this.date = date;
	}

	public Integer getId() {
		return id;
	}

	public Integer getDialogId() {
		return dialogId;
	}

	public UserData getFrom() {
		return from;
	}

	public MessageContent getContent() {
		return content;
	}

	public Date getDate(){
		return date;
	}

	public static class MessageContent {

		private String text;
		private String imageUrl;

		public MessageContent(){}
		public MessageContent(String text, String imageUrl){
			this.text = text;
			this.imageUrl = imageUrl;
		}

		public String getText() {
			return text;
		}

		public String getImageUrl() {
			return imageUrl;
		}
	}
}
