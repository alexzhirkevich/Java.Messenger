package com.alexz.messenger.app.data.model.imp;

import com.alexz.messenger.app.data.model.interfaces.IMediaContent;

public class MediaContent implements IMediaContent {

    private int type;
    private String url;

    public MediaContent(int type, String url){
        this.type = type;
        this.url = url;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUtl(String url) {
        this.url = url;
    }
}
