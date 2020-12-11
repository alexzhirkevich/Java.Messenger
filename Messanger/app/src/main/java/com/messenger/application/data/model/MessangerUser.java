package com.messenger.application.data.model;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class MessangerUser implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;

    public MessangerUser(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.online = online;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }
}
