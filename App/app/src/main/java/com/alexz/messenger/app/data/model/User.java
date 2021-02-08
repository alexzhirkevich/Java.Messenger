package com.alexz.messenger.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.alexz.messenger.app.util.FirebaseUtil;

public class User implements IUser, Parcelable, Comparable<User> {

    private String id;
    private String name;
    private String imageUri;
    private long lastOnline;
    private boolean online;

    public User(){
        this(FirebaseUtil.getCurrentUser());
    }

    public User(String userId, @Nullable String imageUri, String name){
        this.id = userId;
        this.name = name;
        this.imageUri =  imageUri;
    }

    public User(final User u){
        this(u.getId(),u.getImageUri(), u.getName());
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUri = in.readString();
        online = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUri);
        dest.writeByte((byte) (online ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
    public long getLastOnline(){
        return lastOnline;
    }

    @Override
    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public boolean isOnline(){
        return online;
    }

    @Override
    public void setOnline(Boolean online) {
        this.online = online;
    }

    @Override
    public int compareTo(User user) {
        return name.compareTo(user.name);
    }
}