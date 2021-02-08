package com.alexz.messenger.app.ui.userlist;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.IUser;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.RecyclerItemClickListener;
import com.alexz.messenger.app.util.DateUtil;
import com.messenger.app.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class UserListRecyclerAdapter extends RecyclerView.Adapter<UserListRecyclerAdapter.UserViewHolder> {

    private List<User> users = new ArrayList<>();
    private RecyclerItemClickListener<User> clickListener;

    public void setClickListener(RecyclerItemClickListener<User> clickListener) {
        this.clickListener = clickListener;
    }

    public RecyclerItemClickListener<User> getClickListener() {
        return clickListener;
    }

    public void setAll(Collection<? extends User> users){
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    public ArrayList<IUser> getAll(){
        return new ArrayList<>(users);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private final AvatarImageView avatar;
        private final TextView name;
        private final TextView online;

        private User user;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.user_avatar);
            name = itemView.findViewById(R.id.user_name);
            online = itemView.findViewById(R.id.user_last_online);

            itemView.setOnClickListener(view ->{
                if (clickListener != null){
                    clickListener.onItemClick(view,user);
                }
            });

            itemView.setOnLongClickListener(view ->{
                if (clickListener != null){
                    return clickListener.onLongItemClick(view,user);
                }
                return false;
            });
        }

        private void bind(User user){
            this.user = user;
            if (user.getImageUri() != null && !user.getImageUri().isEmpty()){
                avatar.setImageURI(Uri.parse(user.getImageUri()));
            }
            name.setText(user.getName());
            if (user.isOnline()){
                online.setText(online.getResources().getString(R.string.online));
            } else {
                online.setText(DateUtil.getTime(new Date(user.getLastOnline())));
            }
        }
    }
}
