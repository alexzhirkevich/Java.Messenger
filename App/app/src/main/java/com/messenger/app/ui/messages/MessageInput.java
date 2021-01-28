package com.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.messenger.app.R;

public class MessageInput extends LinearLayout {

    private ImageButton send;
    private ImageButton attach;
    private TextView input;

    private OnSendListener sendListener;
    private OnAttachListener attachListener;

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

    public void setOnSendListener(OnSendListener listener){
        this.sendListener = listener;
        send.setOnClickListener( e -> listener.OnSendClicked(send,input));
    }

    public OnSendListener getOnSendListener() {
        return sendListener;
    }

    public void removeOnSendListener(){
        send.setOnClickListener(null);
    }

    public void setOnAttachListener(OnAttachListener listener){
        this.attachListener = listener;
        attach.setOnClickListener( e -> listener.OnAttachClicked(attach,input));
    }

    public OnAttachListener getOnAttachListener() {
        return attachListener;
    }

    public void removeOnAttachListener(){
        attach.setOnClickListener(null);
    }

    public ImageButton getAttachButton() {
        return attach;
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
        attach = findViewById(R.id.button_message_attach);
    }

    @FunctionalInterface
    public interface OnSendListener {
        void OnSendClicked(ImageButton btn, TextView input);
    }

    @FunctionalInterface
    public interface OnAttachListener {
        void OnAttachClicked(ImageButton btn,  TextView input);
    }
}
