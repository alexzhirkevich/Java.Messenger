package com.messenger.application.ui.custom.holder.holders.messages;

import android.view.View;

import com.messenger.application.data.model.DialogMessage;
import com.stfalcon.chatkit.messages.MessageHolders;
public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<DialogMessage> {

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(DialogMessage message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}
