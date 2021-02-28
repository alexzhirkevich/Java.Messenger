package com.alexz.messenger.app.data.model.interfaces;

public interface IMessage {

    String getChatId();
    void setChatId(String chatId);

    String getSenderId();
    void setSenderId(String sender);

    String getSenderName();
    void setSenderName(String name);

    String getSenderPhotoUrl();
    void setSenderPhotoUrl(String uri);

    String getText();
    void setText(String text);

    String getImageUrl();
    void setImageUrl(String url);

    Long getTime();
    void setTime(Long time);

    boolean isPrivate();
    void setPrivate(boolean isPrivate);
}
