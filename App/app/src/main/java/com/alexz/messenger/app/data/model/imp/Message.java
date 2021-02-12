package com.alexz.messenger.app.data.model.imp;

import android.os.Parcel;
import android.os.Parcelable;

import com.alexz.messenger.app.data.model.interfaces.IMessage;
import com.alexz.messenger.app.util.FirebaseUtil;

public class Message extends BaseModel implements IMessage, Parcelable {

    private String chatId;
    private String senderId;
    private String senderName;
    private String senderPhotoUrl;
    private String text;
    private String imageUri;
    private Long time;
    private boolean isPrivate = true;

    private Message(){
        super("");
    }

    public Message(String chatId){
        this(
                chatId,
                FirebaseUtil.getCurrentUser().getId(),
                FirebaseUtil.getCurrentUser().getName(),
                FirebaseUtil.getCurrentUser().getImageUri(),
                System.currentTimeMillis());
    }

    public Message(String chatId, String senderId, String senderName, String  senderPhotoUrl, Long time){
        super("");
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
        super(m.getId());
        senderId = m.senderId;
        senderName = m.senderName;
        senderPhotoUrl = m.senderPhotoUrl;
        text = m.text;
        imageUri = m.imageUri;
        time = m.time;
        isPrivate = m.isPrivate;
    }

    protected Message(Parcel in) {
        super("");
        setId(in.readString());
        chatId = in.readString();
        text = in.readString();
        imageUri = in.readString();
        time = in.readLong();
        senderId = in.readParcelable(User.class.getClassLoader());
        isPrivate = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
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

}
