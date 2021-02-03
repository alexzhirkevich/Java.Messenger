package com.alexz.messenger.app.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.ui.viewmodels.ChatActivityViewModel;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.messenger.app.R;
import com.alexz.messenger.app.ui.common.AvatarImageView;
import com.alexz.messenger.app.ui.common.RecyclerItemClickListener;
import com.alexz.messenger.app.ui.messages.MessageInput;
import com.alexz.messenger.app.ui.messages.MessageFirebaseRecyclerAdapter;

import java.util.Date;

public class    ChatActivity extends AppCompatActivity
    implements MessageInput.OnSendListener, MessageInput.OnAttachListener, PopupMenu.OnMenuItemClickListener {

    private static final String STR_PRIVATE = "private";
    private static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";
    private static final int SELECT_PHOTO = 1001;
    private static final String STR_RECYCLER_DATA = "rec_data";

    private Boolean isPrivate;
    private String chatId;

    private RecyclerView mRecyclerView;
    private MessageFirebaseRecyclerAdapter adapter;;
    private ChatActivityViewModel viewModel;

    public static void startActivity(Context context, String chatID) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chatID);
        context.startActivity(intent);
    }

    public static Intent getIntent(Context context, Chat chat){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID, chat.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final AvatarImageView avatarImageView = findViewById(R.id.chat_avatar);
        final TextView tvChatName = findViewById(R.id.chat_name);

        chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);
        viewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        viewModel.setChatId(chatId);
        setupChatInfo(chatId,avatarImageView,tvChatName);

        setupRecyclerView();
        setupInput();
        setupToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        mRecyclerView.postDelayed(() -> mRecyclerView.smoothScrollToPosition(mRecyclerView.getBottom()),200);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else {
            if (item.getItemId() == R.id.menu_chat_invite){
                ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                if (chatId != null) {
                    ClipData cd = ClipData.newPlainText("Chat id", chatId);
                    cm.setPrimaryClip(cd);
                    Toast.makeText(ChatActivity.this, getString(R.string.action_chat_id_copied), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public void OnSendClicked(ImageButton btn, TextView input) {
        if (input.getText().toString().trim().length() == 0) {
            Toast.makeText(this,R.string.error_empty_message,Toast.LENGTH_SHORT).show();;
            input.setText("");
            return;
        }
        User user = FirebaseUtil.getCurrentUser();
        Message m = new Message(
                chatId,
                user.getId(),
                user.getName(),
                user.getImageUri(),
                new Date().getTime());
        m.setText(input.getText().toString());
        m.setPrivate(isPrivate);
        viewModel.sendMessage(m);
        input.setText("");

        mRecyclerView.postDelayed(() -> mRecyclerView.smoothScrollToPosition(mRecyclerView.getBottom()), 100);
    }

    @Override
    public void OnAttachClicked(ImageButton btn, TextView input) {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

//        switch (requestCode) {
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    Message m = new Message(
//                            new User("5", FirebaseUtil.getCurrentUser().getPhotoUrl().toString(),FirebaseUtil.getCurrentUser().getEmail(), "Name"),
//                            new Date().getTime()
//                    );
//                    m.setImageUrl(imageReturnedIntent.getData().toString());
//                    adapter.add(m);
//                    scrollToBottom(100);
//                }
//        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecyclerView != null){
            RecyclerView.LayoutManager recManager = mRecyclerView.getLayoutManager();
            if (recManager !=null){
                outState.putParcelable(STR_RECYCLER_DATA,recManager.onSaveInstanceState());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setupRecyclerView();
        if (mRecyclerView!=null){
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if  (layoutManager!=null){
                layoutManager.onRestoreInstanceState(savedInstanceState);
            }
        }
    }

    private RecyclerItemClickListener<Message> newItemClickListener() {
        return new RecyclerItemClickListener<Message>() {
            @Override
            public boolean onLongItemClick(View view, Message message) {
                PopupMenu pm = new PopupMenu(ChatActivity.this, view);
                pm.setGravity(Gravity.RIGHT);
                pm.inflate(R.menu.menu_message);
                pm.setOnMenuItemClickListener(e -> {
                    switch (e.getItemId()) {

                        case R.id.message_delete:
                            viewModel.deleteMessage(message);
                            return true;
                        case R.id.message_copy:
                            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            if (message != null) {
                                String text = message.getText();
                                ClipData cd = ClipData.newPlainText("Msg from " + message.getSenderId(), text);
                                cm.setPrimaryClip(cd);
                                Toast.makeText(ChatActivity.this, getString(R.string.action_message_copied), Toast.LENGTH_SHORT).show();
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

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.message_recycler_view);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayout);
        adapter = viewModel.getAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(newItemClickListener());
        adapter.setImageClickListener(new RecyclerItemClickListener<Message>() {
            @Override
            public void onItemClick(View view, Message data) {
                FullscreenImageActivity.startActivity(ChatActivity.this, data.getImageUrl());
            }

            @Override
            public boolean onLongItemClick(View view, Message data) {
                //return newItemClickListener().onLongItemClick(mRecyclerView.getChildAt(adapter.geti);
                return true;
            }
        });
    }

    private void setupInput() {
        final MessageInput input = findViewById(R.id.message_input);
        input.setOnSendListener(this);
        input.setOnAttachListener(this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupChatInfo(String chatId, AvatarImageView avatar, TextView tvChatName){
        if (viewModel!=null){
            viewModel.getChat(chatId).addOnSuccessListener(snapshot -> {
                if (snapshot.exists()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat!=null){
                        isPrivate =  !chat.isGroup();
                        final String avatarUrl = chat.getImageUri();
                        final String chatName = chat.getName();


                        if (tvChatName != null && chatName != null) {
                            tvChatName.post(() -> tvChatName.setText(chatName));
                        }
                        if (avatar != null && avatarUrl != null) {
                            avatar.post( () -> avatar.setImageURI(Uri.parse(avatarUrl)));
                        }
                    } else{
                        new Handler(Looper.getMainLooper()).post(this::errorChatLoad);
                    }
                } else{
                    new Handler(Looper.getMainLooper()).post(this::errorChatLoad);
                }
            }).addOnFailureListener(e -> new Handler(Looper.getMainLooper()).post(this::errorChatLoad));
        }
    }
    private void errorChatLoad(){
        Toast.makeText(ChatActivity.this,R.string.error_chat_not_found,Toast.LENGTH_SHORT).show();
        finish();
    }
}