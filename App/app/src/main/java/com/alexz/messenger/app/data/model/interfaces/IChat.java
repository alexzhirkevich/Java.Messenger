package com.alexz.messenger.app.data.model.interfaces;

import com.alexz.messenger.app.data.model.imp.Message;

public interface IChat {

    String getImageUri();
    void setImageUri(String imageUri);

    String getName();
    void setName(String name);

    Message getLastMessage();
    void setLastMessage(Message lastMessage);

    boolean isGroup();
    void setGroup(Boolean group);

    String getCreatorId();
    void setCreatorId(String creatorId);

    Long getCreationTime();
    void setCreationTime(Long creationTime);
}
