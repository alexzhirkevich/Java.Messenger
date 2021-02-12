package com.alexz.messenger.app.util;

import android.net.Uri;
import android.util.Pair;
import android.webkit.MimeTypeMap;

import com.alexz.messenger.app.data.model.imp.Chat;
import com.alexz.messenger.app.data.model.Result;
import com.alexz.messenger.app.data.model.imp.User;
import com.alexz.messenger.app.data.repo.MessagesRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.messenger.app.R;

import java.util.Date;

public class FirebaseUtil {

    public static final String ID = "id";
    public static final String USERS = "users";
    public static final String CHATS = "chats";
    public static final String INFO = "info";
    public static final String MESSAGES = "messages";
    public static final String LASTMESSAGE = "lastMessage";
    public static final String ONLINE = "online";
    public static final String LASTONLINE = "lastOnline";

    public static FirebaseUser getCurrentFireUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static User getCurrentUser(){
        FirebaseUser user = getCurrentFireUser();
        return new User(user.getUid(),user.getPhotoUrl().toString(),user.getDisplayName());
    }


    public static void setOnline(boolean online) {
        if (FirebaseAuth.getInstance().getUid() != null) {
            User u = FirebaseUtil.getCurrentUser();
            FirebaseDatabase.getInstance().getReference()
                    .child(FirebaseUtil.USERS)
                    .child(u.getId())
                    .child(FirebaseUtil.INFO)
                    .child(FirebaseUtil.ONLINE)
                    .setValue(online);
            if (!online) {
                FirebaseDatabase.getInstance().getReference()
                        .child(FirebaseUtil.USERS)
                        .child(u.getId())
                        .child(FirebaseUtil.INFO)
                        .child(FirebaseUtil.LASTONLINE)
                        .setValue(new Date().getTime());

            }
        }
    }

    public static Result.Future<Chat> getChatInfo(String chatId){
        Result.MutableFuture<Chat> future = new Result.MutableFuture<>();
        MessagesRepository.getChatInfo(chatId)
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Chat chat = snapshot.getValue(Chat.class);
                        future.set(new Result.Success<>(chat));
                    } else {
                        future.set(new Result.Error(R.string.error_chat_not_found));
                    }
                })
                .addOnFailureListener(e -> {
                    future.set(new Result.Error(R.string.error_chat_load));
                });
        return future;
    }

    public static Result.Future<Pair<Uri,StorageReference>> uploadPhoto(Uri path){

        String ext = MimeTypeMap.getFileExtensionFromUrl(path.toString());

        Result.MutableFuture<Pair<Uri,StorageReference>> res = new Result.MutableFuture<>();
        res.setHasProgress(true);

        StorageReference ref = FirebaseStorage.getInstance().getReference()
                .child(FirebaseUtil.getCurrentUser().getId())
                .child(String.valueOf(System.currentTimeMillis()) + "." + ext);
        ref.putFile(path)
                .addOnSuccessListener(taskSnapshot ->
                        taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                .addOnSuccessListener(task ->
                                        res.set(new Result.Success<>(new Pair<>(task,ref))))
                                .addOnFailureListener(e ->
                                        res.set(new Result.Error(R.string.error_upload_file))))
                .addOnFailureListener(e ->
                        res.set(new Result.Error(R.string.error_upload_file)))
                .addOnProgressListener(snapshot ->
                        res.setProgress(100.0 *((UploadTask.TaskSnapshot)snapshot).getBytesTransferred() /((UploadTask.TaskSnapshot)snapshot).getTotalByteCount()));
        return res;
    }
}
