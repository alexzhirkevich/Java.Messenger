package com.messenger.app.ui.messages;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.data.model.Message;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.util.DateUtil;
import com.messenger.app.util.MetrixUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageRecyclerAdapter
        extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {

    private final List<Message> messages = new ArrayList<>();

    public void insert(int idx, Message item) {
        messages.add(idx, item);
        notifyItemInserted(idx);
    }

    public int size() {
        return 0;
    }


    public boolean add(Message item) {
        messages.add(item);
        notifyItemInserted(getItemCount() - 1);
        return false;
    }

    public void set(int idx, Message item) {
        messages.set(idx, item);
        notifyItemChanged(idx);
    }

    public boolean addAll(Collection<? extends Message> dialog) {
        int prev = getItemCount();
        messages.addAll(dialog);
        notifyItemRangeInserted(prev, dialog.size());
        return true;
    }

    public void clear() {
        int l = getItemCount();
        messages.clear();
        notifyItemRangeRemoved(0, l);
    }

    public void remove(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }

    public Message get(int idx){
        return messages.get(idx);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private int id;
        private final AvatarImageView image;
        private final RelativeLayout msgData;
        private final TextView name;
        private final TextView text;
        private final TextView date;

        private String senderName;

        private int topMargin;

        public MessageViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.message_avatar);
            name = itemView.findViewById(R.id.message_sender);
            text = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.message_date);
            msgData = itemView.findViewById(R.id.message_data_layout);
            topMargin = MetrixUtil.dpToPx(itemView.getContext(), 10);
        }

        public void bind(Message message) {

            senderName = message.getSender().getFullName();

            String uri = message.getSender().getImageUri();
            if (uri != null) {
                image.setImageURI(Uri.parse(uri));
            }

            if (senderName != null) {
                name.setText(senderName);
            }
            if (message.getText() != null) {
                text.setText(message.getText());
            }
            if (message.getDate() != null) {
                date.setText(DateUtil.getTime(message.getDate()));
            }
            id = message.getId();


            RelativeLayout.LayoutParams dataParams = (RelativeLayout.LayoutParams) msgData.getLayoutParams();
            RelativeLayout.LayoutParams textParams = (RelativeLayout.LayoutParams) text.getLayoutParams();

            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            dataParams.removeRule(RelativeLayout.END_OF);
            dataParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            textParams.removeRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY);
            textParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
            textParams.setMargins(0, 0, 0, 0);
            dataParams.setMarginEnd(0);
            dataParams.setMarginStart(0);
            name.setText("");

            if (message.isOutcoming()) {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_outcoming));
                msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_outcoming));
                name.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                dataParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.END);
                textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                textParams.setMargins(0, topMargin, 0, 0);
                dataParams.setMarginStart(MetrixUtil.dpToPx(msgData.getContext(), 50));
            } else {
                msgData.setBackgroundColor(msgData.getResources().getColor(R.color.message_incoming));
                dataParams.setMarginEnd(MetrixUtil.dpToPx(msgData.getContext(), 50));
                textParams.addRule(RelativeLayout.TEXT_ALIGNMENT_GRAVITY, GravityCompat.START);
                if (message.isPrivate()) {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_private));
                    image.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    dataParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    textParams.setMargins(0, topMargin, 0, 0);
                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
                } else {
                    msgData.setBackground(AppCompatResources.getDrawable(msgData.getContext(), R.drawable.drawable_message_incoming_group));
                    image.setVisibility(View.VISIBLE);
                    name.setText(senderName);
                    name.setVisibility(View.VISIBLE);
                    dataParams.addRule(RelativeLayout.END_OF, R.id.message_avatar);
                }
            }
            msgData.requestLayout();
        }
    }
}