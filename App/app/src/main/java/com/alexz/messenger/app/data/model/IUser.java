package com.alexz.messenger.app.data.model;

import androidx.annotation.Nullable;

public interface IUser {

    String getId();
    void setId(String userId);

    String getName();
    void setName(String name);

    @Nullable String getImageUri();
    void setImageUri(String uri);

    long getLastOnline();

    void setLastOnline(long lastOnline);

    boolean isOnline();

    void setOnline(Boolean online);
}
