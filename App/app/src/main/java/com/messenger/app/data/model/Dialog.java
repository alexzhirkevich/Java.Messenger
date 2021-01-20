package com.messenger.app.data.model;

import android.net.Uri;
import android.widget.TextView;

import com.messenger.app.ui.AvatarImageView;

import java.util.ArrayList;

public class Dialog implements IDialog {


    private final ArrayList<IMessage> messages;
    private final ArrayList<IUser> users;

    private final Integer id;

    public Dialog(Integer id){
        this.id = id;
        messages = new ArrayList<>();
        users = new ArrayList<>();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public ArrayList<IMessage> getMessages() {
        return messages;
    }

    @Override
    public ArrayList<IUser> getUsers() {
        return users;
    }
}
