package com.messenger.protocol;

import java.util.Date;

public class DialogData {

	private final Integer id;
	private final String imageUri;
	private final String name;
	private final String lastMessage;
	private final UserData lastSender;
	private final Date date;
	private final Integer unreadCount;

	public DialogData(Integer id, String imageUri, String name, String lastMessage, UserData lastSender, Date date, Integer unreadCount){
		this.id = id;
		this.imageUri = imageUri;
		this.name = name;
		this.lastMessage = lastMessage;
		this.lastSender = lastSender;
		this.date = date;
		this.unreadCount = unreadCount;
	}

	public DialogData(DialogData di){
		this(di.getId(), di.getImageUri(),di.getName(),di.getLastMessage(),di.getLastSender(),di.getDate(),di.getUnreadCount());
	}


	public Integer getId() {
		return id;
	}

	public String getImageUri() {
		return imageUri;
	}

	public String getName() {
		return name;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public UserData getLastSender() {
		return lastSender;
	}

	public Integer getUnreadCount() {
		return unreadCount;
	}

	public Date getDate() {
		return date;
	}
}
