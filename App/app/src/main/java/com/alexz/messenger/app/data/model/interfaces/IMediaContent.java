package com.alexz.messenger.app.data.model.interfaces;

public interface IMediaContent {

    int IMAGE = 0;
    int VIDEO = 1;

    int getType();
    void setType(int type);

    String getUrl();
    void setUtl(String url);
}
