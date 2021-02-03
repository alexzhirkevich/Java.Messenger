package com.alexz.messenger.app.ui.messages;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.messenger.app.R;
import com.alexz.messenger.app.ui.common.AvatarImageView;

public class MessageView extends RelativeLayout {

    private RelativeLayout msgData;
    private AvatarImageView avatar;
    private ImageView image;
    private TextView name;
    private TextView text;
    private TextView date;

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

    public AvatarImageView getAvatarView() {
        return avatar;
    }

    public ImageView getImageView() {
        return image;
    }

    public TextView getNameView() {
        return name;
    }

    public TextView getTextView() {
        return text;
    }

    public TextView getDateView() {
        return date;
    }

    public RelativeLayout getMsgDataLayout() {
        return msgData;
    }

    public void setNameClickListener(OnClickListener nameClickListener) {
        name.setOnClickListener(view -> {
            if (nameClickListener != null){
                nameClickListener.onClick(view);
            }
        });
    }

    public void setAvatarClickListener(OnClickListener avatarClickListener) {
        avatar.setOnClickListener(view -> {
            if (avatarClickListener != null){
                avatarClickListener.onClick(view);
            }
        });
    }

    public void setImageClickListener(OnClickListener imageClickListener) {
        image.setOnClickListener(view -> {
            if (imageClickListener != null){
                imageClickListener.onClick(view);
            }
        });
    }
    public void setNameLongClickListener(OnLongClickListener nameClickListener) {
        name.setOnLongClickListener(view -> {
            if (nameClickListener != null){
                return nameClickListener.onLongClick(view);
            }
            return false;
        });
    }

    public void setAvatarLongClickListener(OnLongClickListener avatarClickListener) {
        avatar.setOnLongClickListener(view -> {
            if (avatarClickListener != null){
                return avatarClickListener.onLongClick(view);
            }
            return false;
        });
    }

    public void setImageLongClickListener(OnLongClickListener imageClickListener) {
        image.setOnLongClickListener(view -> {
            if (imageClickListener != null){
                return imageClickListener.onLongClick(view);
            }
            return false;
        });
    }


    private void init(Context context){
        inflate(context, R.layout.item_message,this);
        msgData = findViewById(R.id.message_data_layout);
        avatar = findViewById(R.id.message_avatar);
        image = findViewById(R.id.message_image);
        name = findViewById(R.id.message_sender);
        text = findViewById(R.id.message_text);
        date = findViewById(R.id.message_date);
    }

}
