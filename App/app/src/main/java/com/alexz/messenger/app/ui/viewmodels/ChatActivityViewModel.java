package com.alexz.messenger.app.ui.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.data.model.imp.Message;
import com.alexz.messenger.app.data.model.imp.User;
import com.alexz.messenger.app.data.repo.DialogsRepository;
import com.alexz.messenger.app.data.repo.MessagesRepository;
import com.alexz.messenger.app.ui.common.firerecyclerview.Listenable;
import com.alexz.messenger.app.ui.messages.MessageFirebaseRecyclerAdapter;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;

public class ChatActivityViewModel extends ViewModel implements Listenable {

    private static final String TAG = ChatActivityViewModel.class.getSimpleName();

    private final MutableLiveData<Chat> chatInfoChanged = new MutableLiveData<>();
    private ValueEventListener listener;
    private DatabaseReference chatRef;
    private String chatId;

    public LiveData<Chat> getChatInfoChangedLiveData() {
        return chatInfoChanged;
    }

    @Override
    public void startListening() {
        if (chatId != null) {
            if (chatRef == null || listener == null) {
                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            chatInfoChanged.postValue(snapshot.getValue(Chat.class));
                            if (BuildConfig.DEBUG) {
                                Log.d(TAG, "Chat info updated");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Error while updating chat info");
                    }
                };
                chatRef = DialogsRepository.getChat(chatId);
            }
            chatRef.addValueEventListener(listener);
        }
    }

    @Override
    public void stopListening() {
        if (chatRef!=null && listener != null){
            chatRef.removeEventListener(listener);
        }
    }

    public void setChatId(String chatId){
        this.chatId = chatId;
    }

    public MessageFirebaseRecyclerAdapter getAdapter(){
        if (chatId!=null) {
            FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().
                    setQuery(MessagesRepository.getMessagesReference(chatId), Message.class).build();
            return new MessageFirebaseRecyclerAdapter(options);
        }
        return null;
    }

    public void sendMessage(Message m, @Nullable String replace){
        MessagesRepository.sendMessage(m,replace);
    }

    public void deleteMessage(Message m){
        MessagesRepository.deleteMessage(m);
    }

    public Message emptyMessage(String chatId){
        User user = FirebaseUtil.getCurrentUser();
        return new Message(chatId, user.getId(), user.getName(), user.getImageUri(), System.currentTimeMillis());
    }
}
