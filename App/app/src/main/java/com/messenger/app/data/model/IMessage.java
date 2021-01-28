package com.messenger.app.data.model;

import java.util.Date;

public interface IMessage {

    Integer getId();

    User getSender();

    String getText();
    void setText(String text);

    String getImageUrl();
    void setImageUrl(String url);

    Date getDate();
    void setDate(Date date);

    boolean isPrivate();
    void setPrivate(boolean isPrivate);

    boolean isOutcoming();
    void setOutcoming(boolean outcoming);
}
