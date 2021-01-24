package com.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messenger.app.R;
import com.messenger.app.ui.AvatarImageView;

public class MessageView extends RelativeLayout {

    AvatarImageView avatar;

    TextView name;
    TextView text;
    TextView date;

    boolean incoming;

    public MessageView(Context context) {
        super(context);
        init(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setIncoming(boolean incoming){

    }

    private void init(Context context){
        inflate(context, R.layout.item_message_incoming,this);
        incoming = true;
        avatar = findViewById(R.id.message_avatar);
        name = findViewById(R.id.message_sender);
        text = findViewById(R.id.message_text);
        date = findViewById(R.id.message_date);
    }

}
