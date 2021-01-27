package com.messenger.app.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class User implements IUser{
    private final Integer id;
    private String firstName;
    private String lastName;
    private String imageUri;

    public User(Integer id, @Nullable String imageUri, String firstName, @Nullable String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUri =  imageUri;
    }

    public User(final User u){
        this(u.getId(),u.getImageUri(), u.getFirstName(),u.getLastName());
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public @Nullable String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public  @Nullable String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageUri(String uri) {
        this.imageUri = uri;
    }

    @Override
    public String getFullName() {
        return lastName!=null? firstName + " " + lastName : firstName;
    }
}