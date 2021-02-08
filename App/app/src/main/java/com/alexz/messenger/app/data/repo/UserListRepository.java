package com.alexz.messenger.app.data.repo;

import com.alexz.messenger.app.util.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class UserListRepository {

    public static DatabaseReference getUsers(String chatID){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.CHATS)
                .child(chatID)
                .child(FirebaseUtil.USERS);
    }

    public static DatabaseReference getUser(String userID){
        return FirebaseDatabase.getInstance().getReference()
                .child(FirebaseUtil.USERS)
                .child(userID)
                .child(FirebaseUtil.INFO);
    }
}
