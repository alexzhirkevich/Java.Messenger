package com.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.messenger.app.R;

public class MessageInput extends LinearLayout {

    private ImageButton send;
    private TextView input;

    public MessageInput(Context context) {
        super(context);
        init(context);
    }

    public MessageInput(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageInput(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ImageButton getSendButton() {
        return send;
    }

    public TextView getInputTextView() {
        return input;
    }

    private void init(Context context){
        inflate(context, R.layout.view_message_input,this);
        send = findViewById(R.id.button_message_send);
        input = findViewById(R.id.edit_message_input);
    }
}
