package com.messenger.app.data.model;

import androidx.annotation.Nullable;

public interface IUser {

    Integer getId();

    String getFirstName();
    void setFirstName(String firstName);

    @Nullable String getLastName();
    void setLastName(String lastName);

    @Nullable String getImageUri();
    void setImageUri(String uri);

    String getFullName();
}
