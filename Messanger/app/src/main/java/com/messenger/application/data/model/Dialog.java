package com.messenger.application.data.model;

import com.stfalcon.chatkit.commons.models.IDialog;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Created by troy379 on 04.04.17.
 */
public class Dialog implements IDialog<DialogMessage> {

    private Integer id;
    private Byte[] dialogPhoto;
    private String dialogName;
    private ArrayList<MessageUser> users;
    private DialogMessage lastMessage;

    private int unreadCount;

    public Dialog(Integer id, String name, Byte[] photo,
                  ArrayList<MessageUser> users, DialogMessage lastMessage, int unreadCount) {

        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Byte[] getDialogPhoto() {
        return Arrays.copyOf(dialogPhoto,dialogPhoto.length);
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public ArrayList<MessageUser> getUsers() {
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
