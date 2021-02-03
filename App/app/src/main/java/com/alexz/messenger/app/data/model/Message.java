package com.alexz.messenger.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements IMessage, Parcelable {

    private String id;
    private String chatId;
    private String senderId;
    private String senderName;
    private String senderPhotoUrl;
    private String text;
    private String imageUri;
    private Long time;
    private boolean isPrivate = true;

    public Message(){ }

    public Message(String chatId, String senderId, String senderName, String  senderPhotoUrl, Long time){
        this.id = id;
        this.senderId = senderId;
        this.senderName =senderName;
        this.senderPhotoUrl = senderPhotoUrl;
        this.text = "";
        this.imageUri = "";
        this.time = time;
        this.isPrivate = false;
        this.chatId  = chatId;
    }

    public Message(final Message m){
        senderId = m.senderId;
        text = m.text;
        imageUri = m.imageUri;
        time = m.time;
    }

    protected Message(Parcel in) {
        id = in.readString();
        chatId = in.readString();
        text = in.readString();
        imageUri = in.readString();
        time = in.readLong();
        senderId = in.readParcelable(User.class.getClassLoader());
        isPrivate = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(chatId);
        dest.writeString(text);
        dest.writeString(imageUri);
        dest.writeString(senderId);
        dest.writeLong(time);
        dest.writeByte((byte) (isPrivate ? 1 : 0));
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

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String getSenderId() {
        return senderId;
    }

    @Override
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderPhotoUrl(String senderPhotoUrl) {
        this.senderPhotoUrl = senderPhotoUrl;
    }

    public String getSenderPhotoUrl() {
        return senderPhotoUrl;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getChatId(){
        return chatId;
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
    public Long getTime() {
        return time;
    }

    @Override
    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class UserInfo{

        private String id;
        private String name;
        private String imageUri;

        public UserInfo(final User user){
            this.id = user.getId();
            this.name = user.getName();
            this.imageUri = user.getImageUri();
        }
    }
}
