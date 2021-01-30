package com.messenger.app.data.model;

import androidx.annotation.Nullable;

import com.messenger.app.util.MyGoogleUtils;
import com.messenger.protocol.UserData;

public class User implements IUser{

    public static volatile User current;
    public static volatile String token;

    private final Integer id;
    private String firstName;
    private String lastName;
    private String imageUri;
    private boolean online;

    public User(Integer id, @Nullable String imageUri, String firstName, @Nullable String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUri =  imageUri;
    }

    public User(final User u){
        this(u.getId(),u.getImageUri(), u.getFirstName(),u.getLastName());
    }

    public User(final UserData userData){
        this(userData.getId(),userData.getAvatarUrl(),userData.getFirstName(),userData.getLastName());
    }

    public boolean isOnline(){
        return online;
    }
    public void setOnline(Boolean online) {
        this.online = online;
    }

    public static User getCurrentUser() {
        return current;
    }

    public static void setCurrentUser(User current) {
        User.current = current;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        User.token = token;
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