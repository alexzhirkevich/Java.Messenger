package com.alexz.messenger.app.data.repo;

import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessagesRepository {

    private final DatabaseReference dbReference;

    private MessagesRepository(String chatId) {
        dbReference = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS).child(chatId);
    }

    public static MessagesRepository getInstance(String chatId) {
        return new MessagesRepository(chatId);
    }

    public DatabaseReference getMessagesReference() {
        return dbReference.child(FirebaseUtil.MESSAGES);
    }

    public Task<DataSnapshot> getChatInfo(String chatId){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS)
                .child(chatId)
                .child(FirebaseUtil.INFO)
                .get();
    }

    public void sendMessage(Message m){

        DatabaseReference ref = dbReference.child(FirebaseUtil.MESSAGES).push();
        m.setId(ref.getKey());
        m.setSenderId(FirebaseUtil.getCurrentUser().getId());
        ref.setValue(m);

        dbReference
                .child(FirebaseUtil.INFO)
                .child(FirebaseUtil.LASTMESSAGE)
                .setValue(m);
    }

    public void deleteMessage(Message item) {
        dbReference
                .child(FirebaseUtil.MESSAGES)
                .child(item.getId())
                .removeValue();
    }
}
