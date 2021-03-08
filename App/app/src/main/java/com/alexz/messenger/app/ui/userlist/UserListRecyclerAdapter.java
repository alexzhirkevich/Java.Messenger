package com.alexz.messenger.app.ui.userlist;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alexz.messenger.app.data.model.imp.User;
import com.alexz.messenger.app.data.repo.UserListRepository;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.firerecyclerview.FirebaseMapRecyclerAdapter;
import com.alexz.messenger.app.ui.common.firerecyclerview.FirebaseViewHolder;
import com.alexz.messenger.app.util.DateUtil;
import com.google.firebase.database.Query;
import com.messenger.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class UserListRecyclerAdapter extends FirebaseMapRecyclerAdapter<User, UserListRecyclerAdapter.UserViewHolder> {

    private final String chatId;

    public UserListRecyclerAdapter(String chatId) {
        super(User.class);
        this.chatId = chatId;
    }

    @NonNull
    @Override
    public Query onCreateKeyQuery() {
        return UserListRepository.getUsers(chatId);
    }

    @NonNull
    @Override
    public Query onCreateModelQuery(@NotNull String modelId) {
        return UserListRepository.getUser(modelId);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateClickableViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends FirebaseViewHolder<User> {

        private final AvatarImageView avatar;
        private final TextView name;
        private final TextView online;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.user_avatar);
            name = itemView.findViewById(R.id.user_name);
            online = itemView.findViewById(R.id.user_last_online);
        }

        @Override
        public void bind(User user) {

            super.bind(user);
            {
                if (user.getImageUri() != null && !user.getImageUri().isEmpty()){
                    avatar.setImageURI(Uri.parse(user.getImageUri()));
                }
                name.setText(user.getName());
                if (user.isOnline()){
                    online.setText(online.getResources().getString(R.string.title_online));
                } else {
                    online.setText(DateUtil.getTime(new Date(user.getLastOnline())));
                }
            }
        }
    }
}
