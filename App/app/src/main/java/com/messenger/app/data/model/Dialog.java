package com.messenger.app.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.messenger.protocol.DialogData;
import com.messenger.protocol.UserData;

import java.util.Date;

public class Dialog implements Parcelable {

    private final static int MSG_MAX_LEN = 40;
    private final static int NAME_MAX_LEN = 22;

    private Integer id;
    private String imageUri;
    private String name;
    private String lastMessage;
    private String lastSender;
    private Date date;
    private Integer unreadCount;

    public Dialog(){
        id = 0;
        imageUri = null;
        name = "Dialog Name";
        lastSender = "Last Sender";
        lastMessage = "Last dialog message";
        date = new Date();
        unreadCount = 0;
    }

    public Dialog(Integer id, String imageUri, String name, String lastMessage, String lastSender, Date date, Integer unreadCount){
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastSender = lastSender;
        this.date = date;
        this.unreadCount = unreadCount;
    }

    public Dialog(final Dialog di){
        this(di.getId(), di.getImageUri(),di.getName(),di.getLastMessage(),di.getLastSender(),di.getDate(),di.getUnreadCount());
    }

    public Dialog(final DialogData d){
        this(d.getId(),
                d.getImageUri(),
                d.getName(),
                d.getLastMessage(),
                d.getLastSender().getLastName() == null ? d.getLastSender().getFirstName() : d.getLastSender().getFirstName() + " " + d.getLastSender().getLastName(),
                d.getDate(),
                d.getUnreadCount());
    }
    protected Dialog(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        imageUri = in.readString();
        name = in.readString();
        lastMessage = in.readString();
        lastSender = in.readString();
        date = new Date(in.readLong());
        if (in.readByte() == 0) {
            unreadCount = null;
        } else {
            unreadCount = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(imageUri);
        dest.writeString(name);
        dest.writeString(lastMessage);
        dest.writeString(lastSender);
        dest.writeLong(date.getTime());
        if (unreadCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unreadCount);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dialog> CREATOR = new Creator<Dialog>() {
        @Override
        public Dialog createFromParcel(Parcel in) {
            return new Dialog(in);
        }

        @Override
        public Dialog[] newArray(int size) {
            return new Dialog[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getName() {
        if (name.length() < NAME_MAX_LEN)
            return name;
        else{
            return name.substring(0,NAME_MAX_LEN).concat("...");
        }
    }

    public Date getDate() {
        return date;
    }

    public String getLastMessage() {
        if (lastMessage.length() < MSG_MAX_LEN)
            return lastMessage;
        else{
           return lastMessage.substring(0,MSG_MAX_LEN).concat("...");
        }
    }

    public String getLastSender() {
        if (lastSender.length() < MSG_MAX_LEN)
            return lastSender;
        else{
            return lastSender.substring(0,MSG_MAX_LEN-2).concat("... :");
        }
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastSender(String lastSender) {
        this.lastSender = lastSender;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

}
