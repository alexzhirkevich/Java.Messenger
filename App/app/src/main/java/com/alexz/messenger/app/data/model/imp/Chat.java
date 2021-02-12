package com.alexz.messenger.app.data.model.imp;

import android.os.Parcel;
import android.os.Parcelable;

import com.alexz.messenger.app.data.model.interfaces.IChat;
import com.alexz.messenger.app.util.FirebaseUtil;

public class Chat extends BaseModel implements Parcelable, IChat, Comparable<Chat>{

    private final static int MSG_MAX_LEN = 40;
    private final static int NAME_MAX_LEN = 22;

    private String imageUri;
    private String name;
    private String creatorId;
    private Long creationTime;
    private Message lastMessage;
    private Boolean isGroup;
    public Chat(){
        super("");
        imageUri = null;
        name = "Dialog Name";
        isGroup = false;
    }

    public Chat(String imageUri, String name, boolean group){
        super("");
        this.imageUri = imageUri;
        this.name = name;
        this.isGroup = group;
        this.creatorId = FirebaseUtil.getCurrentUser().getId();
        this.creationTime = System.currentTimeMillis();
    }

    public Chat(final Chat di){
        this(di.getImageUri(),di.getName(),di.isGroup());
        this.creationTime = di.creationTime;
        this.creatorId = di.creatorId;
    }


    protected Chat(Parcel in) {
        super(in.readString());
        imageUri = in.readString();
        name = in.readString();
        creatorId = in.readString();
        if (in.readByte() == 0) {
            creationTime = null;
        } else {
            creationTime = in.readLong();
        }
        lastMessage = in.readParcelable(Message.class.getClassLoader());
        byte tmpIsGroup = in.readByte();
        isGroup = tmpIsGroup == 0 ? null : tmpIsGroup == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(imageUri);
        dest.writeString(name);
        dest.writeString(creatorId);
        if (creationTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(creationTime);
        }
        dest.writeParcelable(lastMessage, flags);
        dest.writeByte((byte) (isGroup == null ? 0 : isGroup ? 1 : 2));
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String getImageUri() {
        return imageUri;
    }

    @Override
    public String getName() {
        if (name.length() < NAME_MAX_LEN)
            return name;
        else{
            return name.substring(0,NAME_MAX_LEN).concat("...");
        }
    }

    @Override
    public Message getLastMessage() {
       return lastMessage;
    }

    @Override
    public boolean isGroup(){
        return isGroup;
    }

    @Override
    public void setGroup(Boolean group) {
        isGroup = group;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getCreatorId() {
        return creatorId;
    }

    @Override
    public Long getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public int compareTo(Chat c) {
        if (lastMessage != null && c.lastMessage != null){
            return c.lastMessage.getTime().compareTo(lastMessage.getTime());
        }
        if (lastMessage != null){
            return  c.creationTime.compareTo(lastMessage.getTime());
        }
        if (c.lastMessage!=null){
            return c.lastMessage.getTime().compareTo(creationTime);
        }
        return c.creationTime.compareTo(creationTime);
    }
}
