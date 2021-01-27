package com.messenger.app.ui.dialogs;

import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DialogItem {

    private final static int MSG_MAX_LEN = 40;
    private final static int NAME_MAX_LEN = 22;

    private Integer id;
    private String imageUri;
    private String name;
    private String lastSender;
    private String lastMessage;
    private String date;
    private Integer unreadCount;

    public DialogItem(){
        id = 0;
        imageUri = null;
        name = "Dialog Name";
        lastSender = "Last Sender";
        lastMessage = "Last dialog message";
        date = "00:00";
        unreadCount = 0;
    }

    public DialogItem(Integer id, String imageUri, String name, String lastMessage, String lastSender, String date, Integer unreadCount){
        this.id = id;
        this.imageUri = imageUri;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastSender = lastSender;
        this.date = date;
        this.unreadCount = unreadCount;
    }

    public DialogItem(DialogItem di){
        this(di.getId(), di.getImageUri(),di.getName(),di.getLastMessage(),di.getLastSender(),di.getDate(),di.getUnreadCount());
    }

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

    public String getDate() {
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

    public void setDate(String date) {
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
