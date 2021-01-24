package com.messenger.app.data.model;

import java.util.Date;

public class Message implements IMessage{

    private final Integer id;
    private String text;
    private User sender;
    private Date date;

    public Message(Integer id, User sender, String text, Date date){
        this.id = id;
        this.sender = new User(sender);
        this.text = text;
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    @Override
    public Integer getId() {
        return id;
    }


    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }
}
