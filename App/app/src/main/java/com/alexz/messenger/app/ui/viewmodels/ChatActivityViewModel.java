package com.alexz.messenger.app.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.data.repo.MessagesRepository;
import com.alexz.messenger.app.ui.messages.MessageFirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class ChatActivityViewModel extends ViewModel {

    private MessagesRepository repo;
    private final MutableLiveData<List<Message>> messages = new MutableLiveData<>();

    public LiveData<List<Message>> getMessages(){
        return messages;
    }

    public void setChatId(String chatId){
        repo = MessagesRepository.getInstance(chatId);
    }

    public MessageFirebaseRecyclerAdapter getAdapter(){
        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().
                setQuery(repo.getMessagesReference(),Message.class).build();
        return new MessageFirebaseRecyclerAdapter(options);
    }

    public Task<DataSnapshot> getChat(String chatId){
        return repo.getChatInfo(chatId);
    }

    public void sendMessage(Message m){
        repo.sendMessage(m);
    }

    public void deleteMessage(Message m){
        repo.deleteMessage(m);
    }
}
