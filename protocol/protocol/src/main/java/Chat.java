package com.messenger.protocol;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@Root
public class Chat implements Serializable {

	private final static long serialVersionUID = 1L;

	@Element
	private Integer id;

	@Element
	private User user1;

	@Element
	private User user2;

	@Element
	private Message [] message;

	private Chat(){}

	public Chat(Chat chat){
		this(chat.getId(),chat.getUser1(),chat.getUser2(),chat.getMessages());
	}

	public Chat(Integer id,User user1, User user2,  Message[] messages){
		setUser1(user1);
		setUser2(user2);
		setMessages(messages);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser1() {
		return new User(user1);
	}

	public void setUser1(User user1) {
		this.user1 = new User(user1);
	}

	public User getUser2() {
		return new User(user2);
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Message[] getMessages() {
		return message!=null ? Arrays.copyOf(message,message.length) : null;
	}

	public void setMessages(Message[] messages) {

		this.message = messages != null ? Arrays.copyOf(messages,messages.length) : null;
		if (this.message == null)
			return;

		Arrays.sort(this.message, new Comparator<Message>() {
			@Override
			public int compare(Message o1, Message o2) {
				int a =  o1.getDate().compareTo(o2.getDate());
				if (a!=0)
					return a;
				return o1.getTime().compareTo(o2.getTime());
			}
		});
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Chat chat = (Chat) o;
		return Objects.equals(user1, chat.user1) &&
				Objects.equals(user2, chat.user2) &&
				Arrays.equals(message, chat.message);
	}

}
