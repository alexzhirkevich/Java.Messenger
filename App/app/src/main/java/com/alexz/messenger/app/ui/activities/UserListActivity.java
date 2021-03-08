package com.alexz.messenger.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.firerecyclerview.LoadingCallback;
import com.alexz.messenger.app.ui.userlist.UserListRecyclerAdapter;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.messenger.app.R;

public class UserListActivity extends BaseActivity {

    private static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";

    private RecyclerView usersRecyclerView;
    private UserListRecyclerAdapter adapter;

    public static void startActivity(Context context, String chatID) {
        Intent starter = new Intent(context, UserListActivity.class);
        starter.putExtra(EXTRA_CHAT_ID,chatID);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        String chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);

        setupRecyclerView(chatId);

        setupToolbar();
        setupChatInfo(chatId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    private void setupRecyclerView(String chatId) {

        usersRecyclerView = findViewById(R.id.user_recycler_view);

        if (usersRecyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            usersRecyclerView.setLayoutManager(layoutManager);
            adapter = new UserListRecyclerAdapter(chatId);
            usersRecyclerView.setAdapter(adapter);
            final ProgressBar pb = findViewById(R.id.user_loading_pb);
            adapter.setLoadingCallback(new LoadingCallback() {
                @Override
                public void onStartLoading() { pb.setVisibility(View.VISIBLE); }

                @Override
                public void onEndLoading() { pb.setVisibility(View.GONE); }
            });
        }
    }
    private void setupChatInfo(String chatId) {
        final AvatarImageView avatarImageView = findViewById(R.id.chat_avatar);
        final TextView tvChatName = findViewById(R.id.chat_name);
        FirebaseUtil.getChatInfo(chatId).addResultListener(new Result.ResultListener<Chat>() {
            @Override
            public void onSuccess(@NonNull Result.ISuccess<Chat> result) {
                Chat chat = result.get();
                if (chat != null) {
                    final String avatarUrl = chat.getImageUri();
                    final String chatName = chat.getName();


                    if (tvChatName != null && chatName != null) {
                        tvChatName.setText(chatName);
                    }
                    if (avatarImageView != null && avatarUrl != null) {
                        avatarImageView.setImageURI(Uri.parse(avatarUrl));
                    }
                }
            }

            @Override
            public void onError(@Nullable Result.IError error) {
                if (error != null) {
                    Toast.makeText(UserListActivity.this, error.getError(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserListActivity.this, R.string.error_chat_load, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}