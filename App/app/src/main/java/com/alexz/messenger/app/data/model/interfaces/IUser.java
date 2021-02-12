package com.alexz.messenger.app.data.model.interfaces;

import androidx.annotation.Nullable;

public interface IUser {

    String getName();
    void setName(String name);

    @Nullable String getImageUri();
    void setImageUri(String uri);

    long getLastOnline();

    void setLastOnline(long lastOnline);

    boolean isOnline();

    void setOnline(Boolean online);
}
