package com.alexz.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.messenger.app.R;

public class MessageInput extends LinearLayout {

    private ImageButton send;
    private ImageButton attach;
    private TextView input;
    private ProgressBar progressBar;

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
        send.setOnClickListener( e -> listener.onSendClicked(send,input));
    }

    public OnSendListener getOnSendListener() {
        return sendListener;
    }

    public void removeOnSendListener(){
        send.setOnClickListener(null);
    }

    public void setOnAttachListener(OnAttachListener listener){
        this.attachListener = listener;
        attach.setOnClickListener( e -> listener.onAttachClicked(attach,input));
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

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    private void init(Context context){
        inflate(context, R.layout.view_message_input,this);
        send = findViewById(R.id.button_message_send);
        input = findViewById(R.id.edit_message_input);
        attach = findViewById(R.id.button_message_attach);
        progressBar = findViewById(R.id.input_attach_progress);

        attach.setColorFilter(ContextCompat.getColor(attach.getContext(),R.color.color_primary));
    }

    @FunctionalInterface
    public interface OnSendListener {
        void onSendClicked(ImageButton btn, TextView input);
    }

    @FunctionalInterface
    public interface OnAttachListener {
        void onAttachClicked(ImageButton btn, TextView input);
    }
}
