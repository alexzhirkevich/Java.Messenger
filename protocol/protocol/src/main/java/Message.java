package com.messenger.protocol;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

@Root
public class Message extends Xml implements Serializable {

	private static final long serialVersionUID = 1L;

	@Element
	private User fromUser;

	@Element
	private User toUser;

	@Element
	private String text;

	@Element
	Date date;

	@Element
	Time time;

	int id;

	private Message(){}

	public Message(Message msg){
		this(msg.getFromUser(), msg.getToUser(), msg.getText(), msg.getDate(),msg.getTime());
	}

	public Message(User fromUser, User toUser, String text, Date date, Time time){
		setFromUser(fromUser);
		setToUser(toUser);
		setText(text);
		setDate(date);
		setTime(time);
		setId(-1);
	}

	public Message(User fromUser, User toUser, String text, Date date, Time time, int id){
		this(fromUser,toUser,text,date,time);
		setId(id);
	}

	public User getFromUser() {
		return new User(fromUser);
	}

	public void setFromUser(User fromUser) {
		this.fromUser = new User(fromUser);
	}

	public User getToUser() {
		return new User(toUser);
	}

	public void setToUser(User toUser) {
		this.fromUser = new User(toUser);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate(){
		return new Date(date.getTime());
	}

	public void setDate(Date date) {
		this.date = new Date(date.getTime());
	}

	public Time getTime(){
		return new Time(time.getTime());
	}

	public void setTime(Time time) {
		this.time = new Time(time.getTime());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFromUserId(int id){
		fromUser.setId(id);
	}

	public void setToUserId(int id){
		toUser.setId(id);
	}
}
