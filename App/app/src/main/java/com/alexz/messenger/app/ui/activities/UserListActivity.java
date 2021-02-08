package com.alexz.messenger.app.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.userlist.UserListRecyclerAdapter;
import com.alexz.messenger.app.ui.viewmodels.UserListViewModel;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.database.DataSnapshot;
import com.messenger.app.R;

import java.util.List;

public class UserListActivity extends BaseActivity {

    private static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";

    private String chatId;
    private RecyclerView usersRecyclerView;
    private UserListRecyclerAdapter adapter;

    private UserListViewModel viewModel;

    public static void startActivity(Context context, String chatID) {
        Intent starter = new Intent(context, UserListActivity.class);
        starter.putExtra(EXTRA_CHAT_ID,chatID);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);
        viewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.setChatId(chatId);

        final ProgressBar pb = findViewById(R.id.user_loading_pb);

        viewModel.getLoadingStarted().observe(this, Void ->{
            pb.setVisibility(View.VISIBLE);
        });

        viewModel.getLoadingEnded().observe(this, Void ->{
            pb.setVisibility(View.INVISIBLE);
        });

        viewModel.getUsersObservable().observe(this, users ->{
            adapter.setAll(users);
        });

        setupRecyclerView();
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
        if (viewModel!=null) {
            viewModel.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel!=null){
            viewModel.stopListening();
        }
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

    private void setupRecyclerView() {

        usersRecyclerView = findViewById(R.id.user_recycler_view);

        if (usersRecyclerView != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            usersRecyclerView.setLayoutManager(layoutManager);

            adapter = new UserListRecyclerAdapter();
            usersRecyclerView.setAdapter(adapter);
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