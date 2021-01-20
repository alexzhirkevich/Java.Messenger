package com.messenger.app.data.model;

public class User implements IUser{
    private final Integer id;
    private String firstName;
    private String lastName;
    private String imageUri;

    public User(Integer id, String firstName){
        this.id = id;
        this.firstName = firstName;
    }
    public User(Integer id, String firstName, String lastName){
        this(id,firstName);
        this.lastName = lastName;
    }

    public User(Integer id, String firstName, String lastName, String imageUri){
        this(id,firstName,lastName);
        this.imageUri = imageUri;
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
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @Override
    public void setImageUri(String uri) {
        this.imageUri = uri;
    }
}