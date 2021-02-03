package com.alexz.messenger.app.data.model;

public interface IMessage {

    String getId();
    void setId(String id);

    String getChatId();
    void setChatId(String chatId);

    String getSenderId();
    void setSenderId(String sender);

    String getText();
    void setText(String text);

    String getImageUrl();
    void setImageUrl(String url);

    Long getTime();
    void setTime(Long time);

    boolean isPrivate();
    void setPrivate(boolean isPrivate);
}
