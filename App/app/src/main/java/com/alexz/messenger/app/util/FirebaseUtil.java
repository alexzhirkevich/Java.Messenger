package com.alexz.messenger.app.util;

import com.alexz.messenger.app.data.model.Message;
import com.alexz.messenger.app.data.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {

    public static final String ID = "id";
    public static final String USERS = "users";
    public static final String CHATS = "chats";
    public static final String INFO = "info";
    public static final String MESSAGES = "messages";
    public static final String LASTMESSAGE = "lastMessage";
    public static final String ONLINE = "online";

    public static FirebaseUser getCurrentFireUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static User getCurrentUser(){
        FirebaseUser user = getCurrentFireUser();
        return new User(user.getUid(),user.getPhotoUrl().toString(),user.getDisplayName());
    }

    public static Task<DataSnapshot> getUserById(String id) {
        return FirebaseDatabase.getInstance().getReference().child(USERS).orderByChild(ID).equalTo(id).get();
    }

    public static Task<DataSnapshot> getLastChatMessage(String chatId) {
        return FirebaseDatabase.getInstance().getReference()
                .child(CHATS).child(chatId).child(MESSAGES).orderByKey().limitToFirst(1).get();
    }



    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }
}
