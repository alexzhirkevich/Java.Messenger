package com.alexz.messenger.app.ui.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.data.repo.MessagesRepository;
import com.alexz.messenger.app.ui.messages.MessageFirebaseRecyclerAdapter;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.messenger.app.R;

import java.util.List;

public class ChatActivityViewModel extends ViewModel {

    private static final String TAG = ChatActivityViewModel.class.getSimpleName();

    private String chatId;

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

    public void sendMessage(Message m){
        MessagesRepository.sendMessage(m);
    }

    public void deleteMessage(Message m){
        MessagesRepository.deleteMessage(m);
    }

    public Message emptyMessage(String chatId){
        User user = FirebaseUtil.getCurrentUser();
        return new Message(chatId, user.getId(), user.getName(), user.getImageUri(), System.currentTimeMillis());
    }

}
