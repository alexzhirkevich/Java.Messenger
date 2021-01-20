package com.messenger.app.data.model;

import java.util.ArrayList;

public interface IDialog {
    
    Integer getId();

    ArrayList<IMessage> getMessages();
    ArrayList<IUser> getUsers();
}
