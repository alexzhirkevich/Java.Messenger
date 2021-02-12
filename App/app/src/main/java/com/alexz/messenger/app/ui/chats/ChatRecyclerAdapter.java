package com.alexz.messenger.app.ui.chats;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.firerecyclerview.FirebaseMapRecyclerAdapter;
import com.alexz.messenger.app.ui.common.firerecyclerview.FirebaseMapViewHolder;
import com.alexz.messenger.app.util.DateUtil;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.firebase.database.Query;
import com.messenger.app.R;

import java.util.Date;

public class ChatRecyclerAdapter extends FirebaseMapRecyclerAdapter<Chat, ChatRecyclerAdapter.ChatViewHolder> {

    public ChatRecyclerAdapter(){
        super(Chat.class);
    }

    @NonNull
    @Override
    public Query onCreateKeyQuery() {
        return DialogsRepository.getChatIds();
    }

    @NonNull
    @Override
    public String onGetFieldForSelection(Chat model) {
        return model.getName();
    }

    @NonNull
    @Override
    public Query onCreateModelQuery(String modelId) {
        return DialogsRepository.getChat(modelId);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateClickableViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog,parent,false);
        return new ChatViewHolder(root);
    }

    static class ChatViewHolder extends FirebaseMapViewHolder<Chat>{

        private final AvatarImageView image;
        private final TextView name;
        private final TextView lastSender;
        private final TextView lastMessage;
        private final TextView date;
        private final TextView unread;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.dialog_avatar);
            name = itemView.findViewById(R.id.dialog_name);
            lastSender = itemView.findViewById(R.id.dialog_last_message_sender);
            lastMessage = itemView.findViewById(R.id.dialog_last_message);
            date = itemView.findViewById(R.id.dialog_last_message_date);
            unread = itemView.findViewById(R.id.dialog_unread_count);
            unread.setVisibility(View.INVISIBLE);
        }

        @Override
        public void bind(Chat chat) {
            super.bind(chat);

            if (chat.getImageUri() != null && !chat.getImageUri().isEmpty()) {
                image.setImageURI(Uri.parse(chat.getImageUri()));
            }
            else {
                image.setImageResource(R.drawable.logo256);
            }
            name.setText(chat.getName());
            if (chat.getLastMessage() != null) {
                lastMessage.setText(chat.getLastMessage().getText());
                if (chat.getLastMessage().getSenderId().equals(FirebaseUtil.getCurrentUser().getId())) {
                    lastSender.setText(R.string.title_you);
                } else {
                    lastSender.setText(chat.getLastMessage().getSenderName());
                }
                lastSender.append(":");
                date.setText(DateUtil.getTime(new Date(chat.getLastMessage().getTime())));
            } else{
                lastMessage.setText("");
                lastSender.setText("");
                date.setText("");
            }
        }
    }
}
