package com.messenger.protocol;

import com.messenger.xml.Xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;


@XmlRootElement
public class Message extends Xml implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement
	private User fromUser;

	@XmlElement
	private User toUser;

	@XmlElement
	private String text;

	@XmlElement
	Date date;

	@XmlElement
	Time time;

	@XmlAttribute
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

	@XmlTransient
	public User getFromUser() {
		return new User(fromUser);
	}

	public void setFromUser(User fromUser) {
		this.fromUser = new User(fromUser);
	}

	@XmlTransient
	public User getToUser() {
		return new User(toUser);
	}

	public void setToUser(User toUser) {
		this.fromUser = new User(toUser);
	}

	@XmlTransient
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@XmlTransient
	public Date getDate(){
		return new Date(date.getTime());
	}

	public void setDate(Date date) {
		this.date = new Date(date.getTime());
	}

	@XmlTransient
	public Time getTime(){
		return new Time(time.getTime());
	}

	public void setTime(Time time) {
		this.time = new Time(time.getTime());
	}

	@XmlTransient
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
