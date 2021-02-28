package com.alexz.messenger.app.data.model.interfaces;

public interface IChannel {
    String getImageUri();
    void setImageUri(String imageUri);

    String getName();
    void setName(String name);

    IPost getLastPost();
    void setLastPost(IPost lastMessage);

    String getCreatorId();
    void setCreatorId(String creatorId);

    Long getCreationTime();
    void setCreationTime(Long creationTime);
}
