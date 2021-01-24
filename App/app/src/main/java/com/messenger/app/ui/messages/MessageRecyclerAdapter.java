package com.messenger.app.ui.messages;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.data.model.Message;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.ui.RecyclerClickListener;
import com.messenger.app.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageRecyclerAdapter
        extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {

    private final List<Message> dialogs = new ArrayList<>();

    RecyclerClickListener.OnItemClickListener clickListener;

    public void insert(int idx, Message item){
        dialogs.add(idx,item);
        notifyItemInserted(idx);
    }

    public int size() {
        return 0;
    }


    public boolean add(Message item){
        dialogs.add(item);
        notifyItemInserted(getItemCount()-1);
        return false;
    }

    public void set(int idx, Message item){
        dialogs.set(idx,item);
        notifyItemChanged(idx);
    }

    public boolean addAll(Collection<? extends Message> dialog) {
        int prev = getItemCount();
        dialogs.addAll(dialog);
        notifyItemRangeInserted(prev, dialog.size());
        return true;
    }


    public void clear() {
        int l = getItemCount();
        dialogs.clear();
        notifyItemRangeRemoved(0,l);
    }

    public void remove(int position) {
        dialogs.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_incoming,parent,false);
        return new MessageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(dialogs.get(position));
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public void setOnItemClickListener(RecyclerClickListener.OnItemClickListener listener){
        this.clickListener = listener;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder
            implements RecyclerClickListener.OnItemClickListener {

        private int id;

        private final AvatarImageView image;

        private final TextView name;
        private final TextView text;
        private final TextView date;


        public MessageViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.dialog_avatar);
            name = itemView.findViewById(R.id.dialog_name);
            text = itemView.findViewById(R.id.dialog_last_message_sender);
            date = itemView.findViewById(R.id.dialog_last_message_date);
        }

        public void bind(Message message){
            image.setImageURI(Uri.parse(message.getSender().getImageUri()));
            name.setText(message.getSender().getFullName());
            text.setText(message.getText());
            date.setText(DateUtil.getTime(message.getDate()));
            id = message.getId();
        }

        @Override
        public void onItemClick(View view, int position) {
           if (clickListener!=null){
               clickListener.onItemClick(view,position);
           }
        }

        @Override
        public void onLongItemClick(View view, int position) {
            clickListener.onLongItemClick(view,position);
        }

    }

}