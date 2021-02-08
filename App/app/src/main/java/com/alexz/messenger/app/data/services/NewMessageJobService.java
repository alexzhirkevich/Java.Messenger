package com.alexz.messenger.app.data.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.alexz.messenger.app.util.NotificationUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;
import com.messenger.app.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NewMessageJobService extends JobService {

    private static final String TAG = NewMessageService.class.getSimpleName();

    private final DialogsRepository repo = DialogsRepository.getInstance();

    private final Map<String, DatabaseReference> chatRefs = new HashMap<>();
    private final Map<String, ValueEventListener> chatListeners = new HashMap<>();
    private Pair<DatabaseReference, ChildEventListener> userChatsRefAndListener;

    private int counter = 0;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        startListening();
        Toast.makeText(this,"start",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        stopListening();
        Toast.makeText(this,"end",Toast.LENGTH_LONG).show();
        return false;
    }

    public static JobInfo getJobInfo(Context context){
        ComponentName serviceComponent = new ComponentName(context, NewMessageJobService.class);
        return new JobInfo.Builder(0,serviceComponent)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .build();

    }

    private void startListening(){
        if (userChatsRefAndListener == null) {

            DatabaseReference ref = repo.getChatIds();

            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String chatId = snapshot.getKey();
                    if (chatId != null) {
                        observeChat(chatId);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "ChatId changed");
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String chatId = snapshot.getValue(String.class);
                    if (chatId != null) {
                        stopObserve(chatId);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "[!!!] ChatId moved from user profile ?!?");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (BuildConfig.DEBUG) {
                        Log.e(TAG, error.toString());
                    }
                }
            };

            ref.addChildEventListener(listener);
            userChatsRefAndListener = new Pair<>(ref, listener);
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "[!] Tried to set up second chats listener. Blocked");
            }
        }

    }

    private void observeChat(String chatId){
        DatabaseReference chatRef = repo.getChat(chatId);
        chatRefs.put(chatId,chatRef);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat chat = snapshot.getValue(Chat.class);
                if (chat !=null){
                    Message lastMessage = chat.getLastMessage();
                    if (lastMessage != null && !lastMessage.getSenderId().equals(FirebaseUtil.getCurrentUser().getId())){
                        notifyNewMessage(chat);
                    }
                } else {
                    repo.removeEmptyChatId(chatId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (BuildConfig.DEBUG){
                    Log.e(TAG,"Failed to load chat " + chatId);
                }
            }
        };
        chatRef.addValueEventListener(listener);
        chatListeners.put(chatId,listener);
    }

    private void stopObserve(String chatId) {
        if (chatId != null) {
            if (chatListeners.containsKey(chatId)) {
                ValueEventListener listener = chatListeners.remove(chatId);
                if (chatRefs.containsKey(chatId)) {
                    DatabaseReference chatRef = chatRefs.get(chatId);
                    if (chatRef != null && listener != null) {
                        chatRef.removeEventListener(listener);
                    }
                }
            }
        }
    }

    private void stopListening() {

        if (userChatsRefAndListener != null) {

            userChatsRefAndListener.first.removeEventListener(userChatsRefAndListener.second);
            userChatsRefAndListener = null;

            for (String chatId : chatRefs.keySet()) {
                stopObserve(chatId);
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "[!] Tried to remove non-exist listener");
            }
        }
    }

    @SuppressLint("CheckResult")
    private void notifyNewMessage( Chat chat){

        NotificationUtil notificationUtil = NotificationUtil.with(NewMessageJobService.this)
                .setTitle(chat.getLastMessage().getSenderName())
                .setText(chat.getLastMessage().getText())
                .setAutoCancel(true)
                .setIntent(ChatActivity.getIntent(NewMessageJobService.this, chat.getId()));

        if (chat.getImageUri() != null && !chat.getImageUri().isEmpty()) {
            Glide.with(NewMessageJobService.this).asBitmap().load(chat.getImageUri()).circleCrop().addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "[!] Failed to load chat bitmap for notification. Notified without image");
                    }
                    notificationUtil.execute(0);
                    return true;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    notificationUtil.setIcon(resource).execute(0);
                    return true;
                }
            }).submit();
        } else {
            Glide.with(NewMessageJobService.this).asBitmap().load(R.drawable.logo256).circleCrop().addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    notificationUtil.execute(0);
                    return true;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    notificationUtil.setIcon(resource).execute(0);
                    return true;
                }
            });
        }
        notificationUtil.execute(counter++);
    }
}
