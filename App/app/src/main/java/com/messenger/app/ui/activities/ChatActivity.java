package com.messenger.app.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.messenger.app.R;
import com.messenger.app.data.model.IMessage;
import com.messenger.app.data.model.Message;
import com.messenger.app.data.model.User;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.ui.RecyclerItemClickListener;
import com.messenger.app.ui.messages.MessageInput;
import com.messenger.app.ui.messages.MessageRecyclerAdapter;
import com.messenger.app.util.MyGoogleUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ChatActivity extends AppCompatActivity
    implements MessageInput.OnSendListener, MessageInput.OnAttachListener, PopupMenu.OnMenuItemClickListener{

    public static final String STR_MESSAGES = "messages";
    public static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";
    public static final String EXTRA_CHAT_NAME = "EXTRA_CHAT_NAME";
    public static final String EXTRA_CHAT_AVATAR = "EXTRA_CHAT_AVATAR";
    private static final int SELECT_PHOTO = 1001;

    private RecyclerView mRecyclerView;
    private MessageRecyclerAdapter adapter;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Integer chatId = getIntent().getIntExtra(EXTRA_CHAT_ID, -1);
        final String chatName = getIntent().getStringExtra(EXTRA_CHAT_NAME);
        final String avatarUrl = getIntent().getStringExtra(EXTRA_CHAT_AVATAR);

        final TextView tvChatName = findViewById(R.id.chat_name);
        final AvatarImageView ivChatAvatar = findViewById(R.id.chat_avatar);
        scrollView = findViewById(R.id.message_scroll);

        setupMessageInput();
        setupRecyclerView();

        if (tvChatName != null && chatName != null) {
            tvChatName.setText(chatName);
        }
        if (ivChatAvatar != null && avatarUrl != null) {
            ivChatAvatar.setImageURI(Uri.parse(avatarUrl));
        }

        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ChatActivityViewModel viewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        viewModel.setChatId(chatId);
        viewModel.getMessages().observe(this, list -> {
            adapter.addAll(list);
        });

        viewModel.updateChat();
    }

    public static void startActivity(Context context, Integer chatID, String chatName, String chatAvatar) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chatID);
        intent.putExtra(EXTRA_CHAT_NAME, chatName);
        intent.putExtra(EXTRA_CHAT_AVATAR, chatAvatar);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void OnSendClicked(ImageButton btn, TextView input) {
        if (input.getText().length() == 0) {
            return;
        }
        Message m = new Message(
                new Random().nextInt(10000),
                new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                new Date());
        m.setText(input.getText().toString());
        m.setPrivate(new Random().nextBoolean());
        m.setOutcoming(new Random().nextBoolean());
        adapter.add(m);
        input.setText("");

        scrollToBottom(100);
    }

    @Override
    public void OnAttachClicked(ImageButton btn, TextView input) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Message m = new Message(
                            new Random().nextInt(),
                            new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                            new Date()
                    );
                    m.setImageUrl(imageReturnedIntent.getData().toString());
                    adapter.add(m);
                    scrollToBottom(100);
                }
        }
    }

    private void scrollToBottom(int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
            runOnUiThread(() -> {
                scrollView.smoothScrollTo(0, mRecyclerView.getBottom());
            });
        }).start();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STR_MESSAGES, new ArrayList<>(adapter.getAll()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scrollView = findViewById(R.id.message_scroll);
        setupRecyclerView();
        setupMessageInput();
        adapter.addAll(savedInstanceState.getParcelableArrayList(STR_MESSAGES));
    }

    private RecyclerItemClickListener<Message> newItemClickListener(){
        return  new RecyclerItemClickListener<Message>() {
            @Override
            public boolean onLongItemClick(View view, Message message) {
                PopupMenu pm = new PopupMenu(ChatActivity.this, view);
                pm.setGravity(Gravity.RIGHT);
                pm.inflate(R.menu.menu_chat);
                pm.setOnMenuItemClickListener(e -> {
                    switch (e.getItemId()) {

                        case R.id.message_delete:
                            adapter.remove(message);
                            return true;
                        case R.id.message_copy:
                            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            if (message != null) {
                                String text = message.getText();
                                ClipData cd = ClipData.newPlainText("Msg from " + message.getSender().getFullName(), text);
                                cm.setPrimaryClip(cd);
                                Toast.makeText(ChatActivity.this, getString(R.string.action_copied), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                    }
                    return false;
                });
                pm.show();
                return true;
            }
        };
    }

    private void setupMessageInput(){
        final MessageInput input = findViewById(R.id.message_input);
        input.setOnSendListener(this);
        input.setOnAttachListener(this);
    }

    private void setupRecyclerView(){
        mRecyclerView = findViewById(R.id.message_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageRecyclerAdapter();
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(newItemClickListener());

        adapter.setImageClickListener(new RecyclerItemClickListener<Message>() {
            @Override
            public void onItemClick(View view, Message data) {
                FullscreenImageActivity.startActivity(ChatActivity.this,data.getImageUrl());
            }

            @Override
            public boolean onLongItemClick(View view, Message data) {
                return newItemClickListener().onLongItemClick(
                        mRecyclerView.getChildAt(adapter.indexOf(data)),data);
            }
        });
    }
}