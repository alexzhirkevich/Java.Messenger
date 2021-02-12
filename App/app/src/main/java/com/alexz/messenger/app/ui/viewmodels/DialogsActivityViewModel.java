package com.alexz.messenger.app.ui.viewmodels;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.data.model.imp.Message;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DialogsActivityViewModel extends ViewModel {

    private final String TAG = DialogsActivityViewModel.class.getSimpleName();

    private final MutableLiveData<List<Chat>> chats = new MutableLiveData<>();
    private final MutableLiveData<Void> loadingEnded = new MutableLiveData<>();
    private final MutableLiveData<Void> loadingStarted = new MutableLiveData<>();

    private final Map<String,Chat> observableChats = new ConcurrentHashMap<>();
    private final Map<String,DatabaseReference> chatRefs = new ConcurrentHashMap<>();
    private final Map<String, ValueEventListener> chatListeners = new ConcurrentHashMap<>();
    private Pair<DatabaseReference, ChildEventListener> userChatsRefAndListener;

    private long chatCount;

    public LiveData<List<Chat>> getChats(){
        return chats;
    }

    public LiveData<Void> getEndLoadingObservable(){
        return loadingEnded;
    }

    public MutableLiveData<Void> getStartLoadingObservable() {
        return loadingStarted;
    }

    public void createChat(Chat chat){
        DialogsRepository.createChat(chat);
    }

    public void startListening(Context context) {
        if (userChatsRefAndListener == null) {

            loadingStarted.postValue(null);

            DatabaseReference ref = DialogsRepository.getChatIds();
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
        DialogsRepository.removeChat(chat);
        stopObserve(chat.getId());
    }

    private void observeChat(String chatId, Context context){
        DatabaseReference chatRef = DialogsRepository.getChat(chatId);
        chatRefs.put(chatId,chatRef);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Chat chat = snapshot.getValue(Chat.class);
                if (chat !=null){
                    observableChats.put(chatId,chat);
                    notifyDataChanged(observableChats.values());
                    Message lastMessage = chat.getLastMessage();
                } else {
                    DialogsRepository.removeEmptyChatId(chatId);
                }

                if (observableChats.size() == chatCount){
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Loading finished");
                    }
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
        Collections.sort(mldChats);
        chats.postValue(mldChats);
    }
}
