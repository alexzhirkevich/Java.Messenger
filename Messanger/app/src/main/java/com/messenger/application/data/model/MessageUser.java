    package com.messenger.application.data.model;

import com.messenger.application.protocol.Message;
import com.messenger.application.protocol.User;
import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class MessageUser extends User implements IUser {

    public MessageUser(MessageUser mu){
        super((User)mu);
    }

    public MessageUser(User u){
        super(u);
    }

}
