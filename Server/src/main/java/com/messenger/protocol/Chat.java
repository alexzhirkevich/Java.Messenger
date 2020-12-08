package com.messenger.protocol;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

@XmlRootElement
public class Chat implements Serializable {

	private final static long serialVersionUID = 1L;

	@XmlElement
	private User user1;

	@XmlElement
	private User user2;

	@XmlElementWrapper(name = "messages")
	@XmlElement
	private Message [] message;

	private Chat(){}

	public Chat(Chat chat){
		this(chat.getUser1(),chat.getUser2(),chat.getMessages());
	}

	public Chat(User user1, User user2, Message[] messages){
		setUser1(user1);
		setUser2(user2);
		setMessages(messages);
	}

	@XmlTransient
	public User getUser1() {
		return new User(user1);
	}

	public void setUser1(User user1) {
		this.user1 = new User(user1);
	}

	@XmlTransient
	public User getUser2() {
		return new User(user2);
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}


	@XmlTransient
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
