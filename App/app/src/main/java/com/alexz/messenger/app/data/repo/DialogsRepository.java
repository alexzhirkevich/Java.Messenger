package com.alexz.messenger.app.data.repo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.ui.activities.ChatActivity;
import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;
import com.messenger.app.R;

public class DialogsRepository {

    public static void createChat(Chat d){
        String userId = FirebaseUtil.getCurrentUser().getId();

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS)
                .push();
        d.setId(userId + ":" + chatRef.getKey());
        d.setCreatorId(userId);

        chatRef = chatRef.getParent().child(d.getId());

        chatRef.child(FirebaseUtil.INFO).setValue(d);

        chatRef.child(FirebaseUtil.USERS)
                .child(userId).setValue("");

        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(d.getCreatorId())
                .child(FirebaseUtil.CHATS)
                .child(d.getId())
                .setValue("");
    }

    public static Result.Future<Chat> findChat(String chatId) {
        Result.MutableFuture<Chat> future = new Result.MutableFuture<>();
        String id = FirebaseUtil.getCurrentUser().getId();
        DatabaseReference addRef = FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(id)
                .child(FirebaseUtil.CHATS)
                .child(chatId);

        addRef.setValue("")
        .addOnSuccessListener(aVoid -> getChat(chatId).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        future.set(new Result.Success<Chat>(snapshot.getValue(Chat.class)));
                        if (BuildConfig.DEBUG) {
                            Log.e("FIND CHAT", "SUCCESS: Chat added");
                        }
                    } else {
                        onChatFindFailure(addRef,future);
                    }
                })
                .addOnFailureListener(e -> onChatFindFailure(addRef,future))
                .addOnCanceledListener(() -> onChatFindFailure(addRef,future)));

        return future;
    }

    public static void removeEmptyChatId(String chatId){
        FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(FirebaseUtil.getCurrentUser().getId())
                .child(FirebaseUtil.CHATS)
                .child(chatId).setValue(null);
    }

    public static DatabaseReference getChatIds(){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(FirebaseUtil.getCurrentUser().getId())
                .child(FirebaseUtil.CHATS);
    }

    public static DatabaseReference getChat(String chatid){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS)
                .child(chatid)
                .child(FirebaseUtil.INFO);
    }

    public static void removeChat(Chat chat) {
        String userId = FirebaseUtil.getCurrentUser().getId();
        if (chat.getCreatorId().equals(userId)) {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseUtil.CHATS)
                    .child(chat.getId()).removeValue();
        } else {
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseUtil.USERS).
                    child(userId).
                    child(FirebaseUtil.CHATS).
                    child(chat.getId()).setValue(null);
        }
    }

    private static void onChatFindFailure(DatabaseReference addRef, Result.MutableFuture<Chat> future){
        addRef.removeValue();
        future.set(new Result.Error(R.string.error_chat_not_found));
        if (BuildConfig.DEBUG) {
            Log.e("FIND CHAT", "FAILURE: Chat added");
        }
    }
}
