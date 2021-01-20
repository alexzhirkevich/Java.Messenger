package com.messenger.app.data.model;

public interface IUser {

    Integer getId();

    String getFirstName();
    void setFirstName(String firstName);

    String getLastName();
    void setLastName(String lastName);

    String getImageUri();
    void setImageUri(String uri);
}
