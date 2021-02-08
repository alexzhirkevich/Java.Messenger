package com.alexz.messenger.app.data.repo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;
import com.messenger.app.R;

public class DialogsRepository {

    private static volatile DialogsRepository instance;
    private DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference()
            .child(FirebaseUtil.CHATS);

    public static DialogsRepository getInstance() {
        if (instance == null){
            instance = new DialogsRepository();
        }
        return instance;
    }

    public void createChat(Chat d){
        String userId = FirebaseUtil.getCurrentUser().getId();

        DatabaseReference ref = dbReference.push();
        d.setId(userId + ":" + ref.getKey());
        d.setCreatorId(userId);
        dbReference
                .child(d.getId())
                .child(FirebaseUtil.INFO).setValue(d);

        dbReference
                .child(d.getId())
                .child(FirebaseUtil.USERS)
                .child(userId).setValue("");
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(d.getCreatorId())
                .child(FirebaseUtil.CHATS)
                .child(d.getId())
                .setValue("");
    }

    public void findChat(String chatId, Context openContext) {

        String id = FirebaseUtil.getCurrentUser().getId();

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(id)
                .child(FirebaseUtil.CHATS)
                .child(chatId)
                .setValue("")
                .addOnSuccessListener(aVoid ->
                        dbReference.child(chatId).child(FirebaseUtil.INFO).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            dbReference.child(chatId).child(FirebaseUtil.USERS).child(id).setValue("");
                            ChatActivity.startActivity(openContext,chatId);
                            if (BuildConfig.DEBUG) {
                                Log.e("FIND CHAT", "SUCCESS: Chat added");
                            }
                        } else {
                            if (BuildConfig.DEBUG) {
                                Toast.makeText(openContext, R.string.error_chat_not_found,Toast.LENGTH_LONG).show();
                                Log.e("FIND CHAT", "ERROR: Incorrect chat id");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (BuildConfig.DEBUG) {
                            Log.e("FIND CHAT", "ERROR: Unknown");
                        }
                    }
                }));
    }

    public void removeEmptyChatId(String chatId){
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(FirebaseUtil.getCurrentUser().getId())
                .child(FirebaseUtil.CHATS)
                .child(chatId).setValue(null);
    }

    public DatabaseReference getChatIds(){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(FirebaseUtil.getCurrentUser().getId())
                .child(FirebaseUtil.CHATS);
    }

    public DatabaseReference getChat(String chatid){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS)
                .child(chatid)
                .child(FirebaseUtil.INFO);
    }

    public void removeChat(Chat chat) {
        String userId = FirebaseUtil.getCurrentUser().getId();
        if (chat.getCreatorId().equals(userId)) {
            dbReference.child(chat.getId()).removeValue();
        } else {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseUtil.USERS).
                    child(userId).
                    child(FirebaseUtil.CHATS).
                    child(chat.getId()).setValue(null);
        }
    }
}
