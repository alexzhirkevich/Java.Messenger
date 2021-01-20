package com.messenger.app.data.model;

public interface IMessage {

    Integer getId();

    Integer getSenderId();

    String getText();
    void setText(String text);
}
