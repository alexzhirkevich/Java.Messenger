package com.messenger.app.data.model;

import java.util.Date;

public class Message implements IMessage{

    private final Integer id;
    private String text;
    private User sender;
    private Date date;
    private boolean isPrivate = true;
    private boolean isOutcoming = false;

    public Message(Integer id, User sender, String text, Date date){
        this.id = id;
        this.sender = new User(sender);
        this.text = text;
        this.date = date;
        this.isPrivate = isPrivate;
    }

    @Override
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean isPrivate() {
        return isPrivate;
    }

    @Override
    public void setOutcoming(boolean outcoming) {
        isOutcoming = outcoming;
    }

    @Override
    public boolean isOutcoming() {
        return isOutcoming;
    }

    @Override
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

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }
}
