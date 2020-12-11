package com.messenger.application.ui.custom.holder.holders.messages;

import android.util.Pair;
import android.view.View;

import com.messenger.application.data.model.DialogMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
/*
 * Created by troy379 on 05.04.17.
 */
public class CustomOutcomingImageMessageViewHolder
        extends MessageHolders.OutcomingImageMessageViewHolder<DialogMessage> {

    public CustomOutcomingImageMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(DialogMessage message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }

    //Override this method to have ability to pass custom data in ImageLoader for loading image(not avatar).
    @Override
    protected Object getPayloadForImageLoader(DialogMessage message) {
        //For example you can pass size of placeholder before loading
        return new Pair<>(100, 100);
    }
}