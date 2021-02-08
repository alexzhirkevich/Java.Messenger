package com.alexz.messenger.app.ui.chats;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.ui.common.RecyclerItemClickListener;
import com.alexz.messenger.app.util.DateUtil;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.messenger.app.R;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.RecyclerClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DialogRecyclerAdapter
        extends RecyclerView.Adapter<DialogRecyclerAdapter.DialogViewHolder> {

    private List<Chat> chats = new ArrayList<>();
    private List<Chat> backup = null;
    private boolean searching = false;

    private RecyclerItemClickListener<Chat> clickListener;

    public boolean insert(int idx, Chat item){
        if (item != null) {
            if (!searching) {
                chats.add(idx, item);
                notifyItemInserted(idx);
            }else {
                backup.add(idx,item);
            }
            return true;
        }
        return false;
    }

    public int size() {
        if (!searching)
            return chats.size();
        return backup.size();
    }

    public boolean add(Chat item){
        if (item != null) {
            if (!searching) {
                chats.add(item);
                notifyItemInserted(getItemCount() - 1);
            } else {
                backup.add(item);
            }
            return true;
        }
        return false;
    }

    public boolean set(int idx, Chat item){
        if (item != null) {
            if (!searching) {
                chats.set(idx, item);
                notifyItemChanged(idx);
            } else {
                backup.set(idx,item);
            }
            return true;
        }
        return false;
    }

    public boolean addAll(Collection<? extends Chat> dialog) {
        if (dialog != null) {
            if (!searching) {
                int prev = getItemCount();
                chats.addAll(dialog);
                notifyItemRangeInserted(prev, dialog.size());
            } else{
                backup.addAll(dialog);
            }
            return true;
        }
        return false;
    }

    public boolean setAll(Collection<? extends Chat> dialog) {
        if (dialog != null) {
            if (searching){
                backup.clear();
                backup.addAll(dialog);
            }else {
                chats.clear();
                chats.addAll(dialog);
                notifyDataSetChanged();
            }
            return true;
        }
        return false;
    }

    public void setVisible(Collection<? extends Chat> dialog){
        if (dialog  != null) {
            if (!searching) {
                searching = true;
                backup = new ArrayList<>(chats);
            }
            chats.clear();;
            chats.addAll(dialog);
            notifyDataSetChanged();
        }
    }

    public void restoreVisibility(){
        chats = backup;
        notifyDataSetChanged();
        searching = false;
    }

    public void clear() {
        int l = chats.size();
        if (searching) {
            backup.clear();
        }
        chats.clear();
        notifyItemRangeRemoved(0,l);
    }

    public void remove(int position) {
        if (searching) {
            backup.remove(chats.get(position));
        }
        chats.remove(position);
        notifyItemRemoved(position);
    }

    public Chat get(int idx){
        if (!searching) {
            return chats.get(idx);
        }
        return backup.get(idx);
    }

    public Chat getVisible(int idx){
        return chats.get(idx);
    }

    public List<Chat> getAll(){
        if (searching)
            return new ArrayList<>(backup);
        else
            return new ArrayList<>(chats);
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog,parent,false);
        return new DialogViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener){
        this.clickListener = listener;
    }


    public class DialogViewHolder extends RecyclerView.ViewHolder {

        private Chat chat;
        private String id;
        private final AvatarImageView image;
        private final TextView name;
        private final TextView lastSender;
        private final TextView lastMessage;
        private final TextView date;
        private final TextView unread;

        public DialogViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.dialog_avatar);
            name = itemView.findViewById(R.id.dialog_name);
            lastSender = itemView.findViewById(R.id.dialog_last_message_sender);
            lastMessage = itemView.findViewById(R.id.dialog_last_message);
            date = itemView.findViewById(R.id.dialog_last_message_date);
            unread = itemView.findViewById(R.id.dialog_unread_count);
            unread.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(view ->{
                if (clickListener != null){
                    clickListener.onItemClick(itemView,chat);
                }
            });
            itemView.setOnLongClickListener(view -> {
                if (clickListener != null) {
                    return clickListener.onLongItemClick(itemView, chat);
                }
                return false;
            });
        }

        public void bind(Chat chat){
            id = chat.getId();
            this.chat = chat;
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
                    lastSender.setText(R.string.you);
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