package com.messenger.app.data.model;

public class Message implements IMessage{

    private final Integer id;
    private final Integer senderId;
    private String text;

    public Message(Integer id, Integer senderId, String text){
        this.id = id;
        this.senderId = senderId;
        this.text = text;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getSenderId() {
        return senderId;
    }


    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
