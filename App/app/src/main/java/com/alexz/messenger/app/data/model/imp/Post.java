package com.alexz.messenger.app.data.model.imp;

import com.alexz.messenger.app.data.model.interfaces.IPost;

import java.util.ArrayList;
import java.util.List;

public class Post extends BaseModel implements IPost {

    private String channelId;
    private String text;
    private List<String> content;
    private long time;

    protected Post(String id) {
        super(id);
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public void setChannelId(String channelId) {
        this.channelId = channelId;
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
    public List<String> getContent() {
        return new ArrayList<>(content);
    }

    @Override
    public void setContent(List<String> content) {
        this.content = new ArrayList<>(content);
    }

    @Override
    public Long getTime() {
        return time;
    }

    @Override
    public void setTime(Long time) {
        this.time = time;
    }
}
