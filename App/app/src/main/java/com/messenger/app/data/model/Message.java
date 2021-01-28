package com.messenger.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Date;

public class Message implements IMessage, Parcelable {

    private final Integer id;
    private String text;
    private String imageUri;
    private User sender;
    private Date date;
    private boolean isPrivate = true;
    private boolean isOutcoming = false;

    public Message(Integer id, User sender, Date date){
        this.id = id;
        this.sender = new User(sender);
        this.text = "";
        this.imageUri = "";
        this.date = date;
        this.isPrivate = isPrivate;
    }

    protected Message(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        text = in.readString();
        imageUri = in.readString();
        isPrivate = in.readByte() != 0;
        isOutcoming = in.readByte() != 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean isPrivate() {
        return isPrivate;
    }

    @Override
    public void setOutcoming(boolean outcoming) {
        isOutcoming = outcoming;
    }

    @Override
    public boolean isOutcoming() {
        return isOutcoming;
    }

    @Override
    public User getSender() {
        return sender;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getImageUrl() {
        return imageUri;
    }

    @Override
    public void setImageUrl(String url) {
        this.imageUri = url;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(text);
        parcel.writeString(imageUri);
        parcel.writeByte((byte) (isPrivate ? 1 : 0));
        parcel.writeByte((byte) (isOutcoming ? 1 : 0));
    }
}
