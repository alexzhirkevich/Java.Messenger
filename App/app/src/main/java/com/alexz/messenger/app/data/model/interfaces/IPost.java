package com.alexz.messenger.app.data.model.interfaces;

import java.util.List;

public interface IPost {

    String getChannelId();
    void setChannelId(String channelId);

    String getText();
    void setText(String text);

    List<String> getContent();
    void setContent(List<String> content);

    Long getTime();
    void setTime(Long time);
}
