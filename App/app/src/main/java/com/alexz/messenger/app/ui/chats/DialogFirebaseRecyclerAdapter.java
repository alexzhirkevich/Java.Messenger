package com.alexz.messenger.app.ui.chats;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.RecyclerClickListener;
import com.alexz.messenger.app.util.DateUtil;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.messenger.app.R;

import java.util.Date;

public class DialogFirebaseRecyclerAdapter
        extends FirebaseRecyclerAdapter<Chat, DialogFirebaseRecyclerAdapter.DialogViewHolder> {

//    private List<Chat> chats = new ArrayList<>();
//    private List<Chat> backup = null;
//    private boolean searching = false;

    RecyclerClickListener.OnItemClickListener clickListener;

    public DialogFirebaseRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
        super(options);
    }

//    public boolean insert(int idx, Chat item){
//        if (item != null) {
//            if (!searching) {
//                chats.add(idx, item);
//                notifyItemInserted(idx);
//            }else {
//                backup.add(idx,item);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public int size() {
//        if (!searching)
//            return chats.size();
//        return backup.size();
//    }
//
//    public boolean add(Chat item){
//        if (item != null) {
//            if (!searching) {
//                chats.add(item);
//                notifyItemInserted(getItemCount() - 1);
//            } else {
//                backup.add(item);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public boolean set(int idx, Chat item){
//        if (item != null) {
//            if (!searching) {
//                chats.set(idx, item);
//                notifyItemChanged(idx);
//            } else {
//                backup.set(idx,item);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public boolean addAll(Collection<? extends Chat> dialog) {
//        if (dialog != null) {
//            if (!searching) {
//                int prev = getItemCount();
//                chats.addAll(dialog);
//                notifyItemRangeInserted(prev, dialog.size());
//            } else{
//                backup.addAll(dialog);
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public boolean setAll(Collection<? extends Chat> dialog) {
//        if (dialog != null) {
//            chats.clear();
//            chats.addAll(dialog);
//            notifyDataSetChanged();
//            return true;
//        }
//        return false;
//    }
//
//    public void setVisible(Collection<? extends Chat> dialog){
//        if (dialog  != null) {
//            if (!searching) {
//                searching = true;
//                backup = new ArrayList<>(chats);
//                setAll(dialog);
//            }
//            setAll(dialog);
//        }
//    }
//
//    public void restoreVisibility(){
//        chats = backup;
//        notifyDataSetChanged();
//        searching = false;
//    }
//
//    public void clear() {
//        int l = chats.size();
//        if (searching) {
//            backup.clear();
//        }
//        chats.clear();
//        notifyItemRangeRemoved(0,l);
//    }
//
//    public void remove(int position) {
//        if (searching) {
//            backup.remove(chats.get(position));
//        }
//        chats.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public Chat get(int idx){
//        if (!searching) {
//            return chats.get(idx);
//        }
//        return backup.get(idx);
//    }
//
//    public Chat getVisible(int idx){
//        return chats.get(idx);
//    }
//
//    public List<Chat> getAll(){
//        if (searching)
//            return new ArrayList<>(backup);
//        else
//            return new ArrayList<>(chats);
//    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialog,parent,false);
        return new DialogViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull DialogViewHolder holder, int position, @NonNull Chat model) {
        holder.bind(model);
    }

    public void setOnItemClickListener(RecyclerClickListener.OnItemClickListener listener){
        this.clickListener = listener;
    }


    public class DialogViewHolder extends RecyclerView.ViewHolder
            implements RecyclerClickListener.OnItemClickListener {

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
        }

        public void bind(Chat chat){

            if (chat.getLastMessage() != null) {
                if (chat.getLastMessage().getSenderId() == FirebaseUtil.getCurrentUser().getId()){
                    lastSender.setText(lastSender.getContext().getString(R.string.you)+ ":");
                } else {
                    FirebaseUtil.getUserById(chat.getLastMessage().getSenderId()).addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    lastSender.setText((snapshot.getChildren().iterator().next().getValue(User.class)).getName() + ":"));
                        }
                    });
                }
            }
            id = chat.getId();
            image.setImageURI(Uri.parse(chat.getImageUri()));
            name.setText(chat.getName());
            if (chat.getLastMessage() != null) {
                lastMessage.setText(chat.getLastMessage().getText());
                date.setText(DateUtil.getTime(new Date(chat.getLastMessage().getTime())));
            } else{
                lastMessage.setText("");
                lastSender.setText("");
                date.setText("");
            }

            unread.setVisibility(View.INVISIBLE);
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