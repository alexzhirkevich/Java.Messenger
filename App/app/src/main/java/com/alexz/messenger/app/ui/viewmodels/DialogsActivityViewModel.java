package com.alexz.messenger.app.ui.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogsActivityViewModel extends ViewModel {

    private final String TAG = DialogsActivityViewModel.class.getSimpleName();

    private DialogsRepository repo = DialogsRepository.getInstance();

    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<Void> loadingEnded = new MutableLiveData<>();

    private final Map<String,Chat> observableChats = new HashMap<>();
    private final Map<String,DatabaseReference> chatRefs = new HashMap<>();
    private final Map<String, ValueEventListener> chatListeners = new HashMap<>();
    private Pair<DatabaseReference, ChildEventListener> userChatsRefAndListener;

    private long chatCount;

    private boolean listening = false;

    public LiveData<List<Chat>> getChats(){
        return chats;
    }

    public LiveData<Void> getEndLoadingObservable(){
        return loadingEnded;
    }

    public void setOnline(boolean online){
        repo.setOnline(online);
    }

    public void createChat(Chat chat){
        repo.createChat(chat);
    }

    public void startListening(Context context) {
        if (userChatsRefAndListener == null) {

            DatabaseReference ref = repo.getChatIds();
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatCount = snapshot.getChildrenCount();
                    if (chatCount == 0){
                        loadingEnded.postValue(null);
                    } else {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "Loading " + chatCount + " dialogs...");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    chatCount = -1;
                    if (BuildConfig.DEBUG){
                        Log.d(TAG,"Failed to get dialogs count");
                    }
                }
            });

            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String chatId = snapshot.getKey();
                    if (chatId != null) {
                        observeChat(chatId, context);
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

    @SuppressLint("CheckResult")
    public void notifyNewMessage(Context context, Chat chat){
        Glide.with(context).asBitmap().load(chat.getImageUri()).circleCrop().addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "[!] Failed to load chat bitmap for notification. Notified without image");
                }
                NotificationUtil.with(context)
                        .setTitle(chat.getLastMessage().getSenderName())
                        .setText(chat.getLastMessage().getText())
                        .setAutoCancel(true)
                        .setIntent(ChatActivity.getIntent(context, chat))
                        .execute(0);
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                NotificationUtil.with(context)
                        .setIcon(resource)
                        .setTitle(chat.getLastMessage().getSenderName())
                        .setText(chat.getLastMessage().getText())
                        .setAutoCancel(true)
                        .setIntent(ChatActivity.getIntent(context, chat))
                        .execute(0);
                return  true;
            }
        }).submit();
    }

    public void stopListening() {

        if (userChatsRefAndListener != null) {

            userChatsRefAndListener.first.removeEventListener(userChatsRefAndListener.second);
            userChatsRefAndListener = null;

            for (String chatId : chatRefs.keySet()) {
                stopObserve(chatId);
            }
            observableChats.clear();
            notifyDataChanged(observableChats.values());
        } else {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, "[!] Tried to remove non-exist listener");
            }
        }
    }

    public void removeChat(Chat chat) {
        repo.removeChat(chat);
        stopObserve(chat.getId());
    }

    private void observeChat(String chatId, Context context){
        DatabaseReference chatRef = repo.getChat(chatId);
        chatRefs.put(chatId,chatRef);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat chat = snapshot.getValue(Chat.class);
                if (chat !=null){
                    observableChats.put(chatId,chat);
                    notifyDataChanged(observableChats.values());
                    Message lastMessage = chat.getLastMessage();
                    if (lastMessage != null && !lastMessage.getSenderId().equals(FirebaseUtil.getCurrentUser().getId())){
                        notifyNewMessage(context,chat);
                    }
                } else {
                    repo.removeEmptyChatId(chatId);
                }

                if (observableChats.size() == chatCount){
                    loadingEnded.postValue(null);
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
                if (observableChats.containsKey(chatId)){
                    observableChats.remove(chatId);
                    notifyDataChanged(observableChats.values());
                }
            }
        }
    }

    private void notifyDataChanged(Collection<Chat> newData){
        List<Chat> mldChats = chats.getValue();
        if (mldChats == null){
            mldChats = new ArrayList<>();
        }
        mldChats.clear();
        if (newData != null) {
            mldChats.addAll(newData);
        }
        chats.postValue(mldChats);
    }
}
