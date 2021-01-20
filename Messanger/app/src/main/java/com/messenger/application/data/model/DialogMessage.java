package com.messenger.application.data.model;

import com.messenger.application.protocol.Message;
import com.messenger.application.protocol.User;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Arrays;
import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
public class DialogMessage implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType /*and this one is for custom content type (in this case - voice message)*/ {

    private Integer id;
    private String text;
    private Date createdAt;
    private MessageUser user;
    private Byte[] image;
    private Voice voice;

    public DialogMessage(Integer id, MessageUser user, String text) {
        this(id, user, text, new Date());
    }

    public DialogMessage(Integer id, MessageUser user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = new MessageUser(user);
        this.createdAt = createdAt;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public MessageUser getUser() {
        return this.user;
    }

    @Override
    public Byte[] getImage() {
        return image == null ? null : Arrays.copyOf(image,image.length);
    }

    public Voice getVoice() {
        return voice;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Byte[] image) {
        this.image = image != null? Arrays.copyOf(image,image.length):null;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static class Voice {

        private String url;
        private int duration;

        public Voice(String url, int duration) {
            this.url = url;
            this.duration = duration;
        }

        public String getUrl() {
            return url;
        }

        public int getDuration() {
            return duration;
        }
    }
}
