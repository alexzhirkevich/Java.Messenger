package com.messenger.application.data.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;

/*
 * Created by troy379 on 04.04.17.
 */
public class Dialog implements IDialog<DialogMessage> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private ArrayList<MessangerUser> users;
    private DialogMessage lastMessage;

    private int unreadCount;

    public Dialog(String id, String name, String photo,
                  ArrayList<MessangerUser> users, DialogMessage lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<MessangerUser> getUsers() {
        return users;
    }

    @Override
    public DialogMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(DialogMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
