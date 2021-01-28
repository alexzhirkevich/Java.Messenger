package com.messenger.app.ui.activities;

import android.app.ActionBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.messenger.app.data.model.IMessage;
import com.messenger.app.data.model.Message;
import com.messenger.app.data.model.User;
import com.messenger.app.ui.dialogs.DialogItem;
import com.messenger.app.util.MyGoogleUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatActivityViewModel extends ViewModel {

    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private Integer chatId = -1;
    private String chatName = "";

    public LiveData<List<Message>> getMessages(){
        return messages;
    }

    public void setChatId(Integer chatId){
        this.chatId = chatId;
    }

    public void updateChat() {
        List<Message> msgs = new ArrayList<>();
        Message m = new Message(
                new Random().nextInt(10000),
                new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                new Date());
        m.setText( "This is group incoming message");
        m.setPrivate(false);
        m.setOutcoming(false);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is private incoming message");
        m.setPrivate(true);
        m.setOutcoming(false);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is outcoming message");
        m.setPrivate(false);
        m.setOutcoming(true);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                new Date());
        m.setText("This is group incoming\nmultiline message");
        m.setPrivate(false);
        m.setOutcoming(false);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is private incoming\nmultiline message");
        m.setPrivate(true);
        m.setOutcoming(false);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is outcoming\nmultiline message");
        m.setPrivate(false);
        m.setOutcoming(true);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is example.com message\n[Double tap]");
        m.setPrivate(false);
        m.setOutcoming(true);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                new Date());
        m.setText("This is incoming wide image message");
        m.setImageUrl("https://miro.medium.com/max/11730/0*ihTZPO4iffJ8n69_");
        m.setPrivate(false);
        m.setOutcoming(false);
        msgs.add(m);

        m = new Message(
                new Random().nextInt(10000),
                new User(5, null, "Name", null),
                new Date());
        m.setText("This is outcoming long image message");
        m.setImageUrl("https://images.wallpaperscraft.ru/image/astronavt_kosmonavt_art_129529_1080x1920.jpg");
        m.setPrivate(true);
        m.setOutcoming(true);
        msgs.add(m);

        messages.setValue(msgs);
    }
}
