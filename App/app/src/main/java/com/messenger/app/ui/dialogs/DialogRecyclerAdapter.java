package com.messenger.app.ui.dialogs;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.data.model.Dialog;
import com.messenger.app.ui.common.AvatarImageView;
import com.messenger.app.ui.common.RecyclerClickListener;
import com.messenger.app.util.DateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DialogRecyclerAdapter
        extends RecyclerView.Adapter<DialogRecyclerAdapter.DialogViewHolder> {

    private List<Dialog> dialogs = new ArrayList<>();
    private List<Dialog> backup = null;
    private boolean searching = false;

    RecyclerClickListener.OnItemClickListener clickListener;

    public boolean insert(int idx, Dialog item){
        if (item != null) {
            if (!searching) {
                dialogs.add(idx, item);
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
            return dialogs.size();
        return backup.size();
    }

    public boolean add(Dialog item){
        if (item != null) {
            if (!searching) {
                dialogs.add(item);
                notifyItemInserted(getItemCount() - 1);
            } else {
                backup.add(item);
            }
            return true;
        }
        return false;
    }

    public boolean set(int idx, Dialog item){
        if (item != null) {
            if (!searching) {
                dialogs.set(idx, item);
                notifyItemChanged(idx);
            } else {
                backup.set(idx,item);
            }
            return true;
        }
        return false;
    }

    public boolean addAll(Collection<? extends Dialog> dialog) {
        if (dialog != null) {
            if (!searching) {
                int prev = getItemCount();
                dialogs.addAll(dialog);
                notifyItemRangeInserted(prev, dialog.size());
            } else{
                backup.addAll(dialog);
            }
            return true;
        }
        return false;
    }

    public boolean setAll(Collection<? extends Dialog> dialog) {
        if (dialog != null) {
            dialogs.clear();
            dialogs.addAll(dialog);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public void setVisible(Collection<? extends Dialog> dialog){
        if (dialog  != null) {
            if (!searching) {
                searching = true;
                backup = new ArrayList<>(dialogs);
                setAll(dialog);
            }
            setAll(dialog);
        }
    }

    public void restoreVisibility(){
        dialogs = backup;
        notifyDataSetChanged();
        searching = false;
    }

    public void clear() {
        int l = dialogs.size();
        if (searching) {
            backup.clear();
        }
        dialogs.clear();
        notifyItemRangeRemoved(0,l);
    }

    public void remove(int position) {
        if (searching) {
            backup.remove(dialogs.get(position));
        }
        dialogs.remove(position);
        notifyItemRemoved(position);
    }

    public Dialog get(int idx){
        if (!searching) {
            return dialogs.get(idx);
        }
        return backup.get(idx);
    }

    public Dialog getVisible(int idx){
        return dialogs.get(idx);
    }

    public List<Dialog> getAll(){
        if (searching)
            return new ArrayList<>(backup);
        else
            return new ArrayList<>(dialogs);
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

    public void setOnItemClickListener(RecyclerClickListener.OnItemClickListener listener){
        this.clickListener = listener;
    }


    public class DialogViewHolder extends RecyclerView.ViewHolder
            implements RecyclerClickListener.OnItemClickListener {

        private Integer id;
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

        public void bind(Dialog dialog){
            id = dialog.getId();
            image.setImageURI(Uri.parse(dialog.getImageUri()));
            name.setText(dialog.getName());
            lastMessage.setText(dialog.getLastMessage());
            lastSender.setText(dialog.getLastSender());
            date.setText(DateUtil.getTime(dialog.getDate()));
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