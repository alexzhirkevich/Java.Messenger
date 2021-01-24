package com.messenger.app.data.model;

public interface IMessage {

    Integer getId();

    User getSender();

    String getText();
    void setText(String text);
}
