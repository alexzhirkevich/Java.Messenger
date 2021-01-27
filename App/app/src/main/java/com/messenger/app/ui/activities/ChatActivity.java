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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.messenger.app.R;
import com.messenger.app.data.model.Message;
import com.messenger.app.data.model.User;
import com.messenger.app.ui.AvatarImageView;
import com.messenger.app.ui.RecyclerClickListener;
import com.messenger.app.ui.dialogs.DialogItem;
import com.messenger.app.ui.messages.MessageInput;
import com.messenger.app.ui.messages.MessageRecyclerView;
import com.messenger.app.util.MyGoogleUtils;
import com.messenger.app.util.VibrateUtil;

import java.util.Date;
import java.util.Random;

public class ChatActivity extends AppCompatActivity
        implements RecyclerClickListener.OnItemClickListener {

    public static final String EXTRA_CHAT_ID = "EXTRA_CHAT_ID";
    public static final String EXTRA_CHAT_NAME = "EXTRA_CHAT_NAME";
    public static final String EXTRA_CHAT_AVATAR = "EXTRA_CHAT_AVATAR";

    private ChatActivityViewModel viewModel;
    private MessageRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Integer chatId = getIntent().getIntExtra(EXTRA_CHAT_ID,-1);
        final String chatName = getIntent().getStringExtra(EXTRA_CHAT_NAME);
        final String avatarUrl = getIntent().getStringExtra(EXTRA_CHAT_AVATAR);

        final TextView tvChatName = findViewById(R.id.chat_name);
        final AvatarImageView ivChatAvatar = findViewById(R.id.chat_avatar);
        final MessageInput input = findViewById(R.id.message_input);
        final NestedScrollView scrollView  = findViewById(R.id.message_scroll);
        mRecyclerView = findViewById(R.id.message_recycler_view);

        if (tvChatName != null && chatName != null){
            tvChatName.setText(chatName);
        }
        if (ivChatAvatar != null && avatarUrl != null){
            ivChatAvatar.setImageURI(Uri.parse(avatarUrl));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        viewModel.setChatId(chatId);
        viewModel.getMessages().observe(this,list -> {
            mRecyclerView.getAdapter().addAll(list);
        });

        input.getSendButton().setOnClickListener(v ->{
            TextView text = input.getInputTextView();

            if (text.getText().length()==0){
                return;
            }

            com.messenger.app.data.model.Message m = new Message(
                    new Random().nextInt(10000),
                    new User(5, MyGoogleUtils.getAccount().getPhotoUrl().toString(), "Name", null),
                    text.getText().toString(),
                    new Date());
            m.setPrivate(new Random().nextBoolean());
            m.setOutcoming(new Random().nextBoolean());
            mRecyclerView.getAdapter().add(m);
            text.setText("");

            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
                runOnUiThread(() -> {
                    scrollView.smoothScrollTo(0,mRecyclerView.getBottom());
                });
            }).start();

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerClickListener(this, mRecyclerView, this));
        viewModel.updateChat();
    }

    public static void startActivity(Context context, Integer chatID, String chatName, String chatAvatar){
        Intent intent = new Intent(context,ChatActivity.class);
        intent.putExtra(EXTRA_CHAT_ID,chatID);
        intent.putExtra(EXTRA_CHAT_NAME,chatName);
        intent.putExtra(EXTRA_CHAT_AVATAR,chatAvatar);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onLongItemClick(View view, int position) {
        PopupMenu pm = new PopupMenu(this,view);
        pm.setGravity(Gravity.RIGHT);
        pm.inflate(R.menu.menu_chat);
        pm.setOnMenuItemClickListener(e ->{
            switch (e.getItemId()) {

                case R.id.message_delete:
                    mRecyclerView.getAdapter().remove(position);
                    return true;
                case R.id.message_copy:
                    ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    Message msg = mRecyclerView.getAdapter().get(position);
                    if (msg != null) {
                        String text = msg.getText();
                        ClipData cd = ClipData.newPlainText("Msg from " +msg.getSender().getFullName(), text);
                        cm.setPrimaryClip(cd);
                        Toast.makeText(this, getString(R.string.action_copied), Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        });
        VibrateUtil.with(this).vibrate(20,VibrateUtil.POWER_LOW);
        pm.show();
    }
}