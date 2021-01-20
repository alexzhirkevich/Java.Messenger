package com.messenger.app.ui.dialog;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.ui.RippleView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DialogRecyclerAdapter
        extends RecyclerView.Adapter<DialogRecyclerAdapter.DialogViewHolder> {

    private final List<DialogItem> dialogs = new ArrayList<>();

    DialogItemTouchListener.OnItemClickListener clickListener;

    public void insert(int idx, DialogItem item){
        dialogs.add(idx,item);
        notifyItemInserted(idx);
    }

    public void add(DialogItem item){
        dialogs.add(item);
        notifyItemInserted(getItemCount()-1);
    }

    public void set(int idx, DialogItem item){
        dialogs.set(idx,item);
        notifyItemChanged(idx);
    }

    public void addAll(Collection<DialogItem> dialog) {
        int prev = getItemCount();
        dialogs.addAll(dialog);
        notifyItemRangeInserted(prev, dialog.size());
    }

    public void clearItems() {
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
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog,parent,false);
        return new DialogViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        holder.bind(dialogs.get(position));
    }

    @Override
    public int getItemCount() {
        return dialogs.size();
    }

    public void setOnItemClickListener(DialogItemTouchListener.OnItemClickListener listener){
        this.clickListener = listener;
    }


    public class DialogViewHolder extends RecyclerView.ViewHolder
            implements DialogItemTouchListener.OnItemClickListener {

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
        }

        public void bind(DialogItem dialog){
            image.setImageURI(Uri.parse(dialog.getImageUri()));
            name.setText(dialog.getName());
            lastMessage.setText(dialog.getLastMessage());
            lastSender.setText(dialog.getLastSender());
            date.setText(dialog.getDate());
            unread.setText(dialog.getUnreadCount().toString());
            unread.setVisibility(dialog.getUnreadCount() <=0? View.INVISIBLE:View.VISIBLE);
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