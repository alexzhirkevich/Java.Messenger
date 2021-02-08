package com.alexz.messenger.app.ui.viewmodels;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexz.messenger.app.data.model.Chat;
import com.alexz.messenger.app.data.model.IUser;
import com.alexz.messenger.app.data.model.User;
import com.alexz.messenger.app.data.repo.UserListRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.messenger.app.BuildConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserListViewModel extends ViewModel {

    private static final String TAG = UserListViewModel.class.getSimpleName();

    private String chatId;
    private long count;
    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<Void> loadingEnded = new MutableLiveData<>();
    private final MutableLiveData<Void> loadingStarted = new MutableLiveData<>();

    private final Map<String, User> observableUsers = new ConcurrentHashMap<>();
    private final Map<String, DatabaseReference> userRefs = new ConcurrentHashMap<>();
    private final Map<String, ValueEventListener> userListeners = new ConcurrentHashMap<>();
    private Pair<DatabaseReference, ChildEventListener> chatRefAndListener;

    public LiveData<List<User>> getUsersObservable() {
        return users;
    }

    public LiveData<Void> getLoadingEnded() {
        return loadingEnded;
    }

    public LiveData<Void> getLoadingStarted() {
        return loadingStarted;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void startListening(){
        if (chatRefAndListener != null || chatId == null){
            return;
        }
        loadingStarted.postValue(null);

        DatabaseReference ref = UserListRepository.getUsers(chatId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    count = snapshot.getChildrenCount();
                    if (count == 0) {
                        loadingEnded.postValue(null);
                    } else {
                        if (BuildConfig.DEBUG) {
                            Log.d(TAG, "Loading " + count + " users...");
                        }
                    }
                } else{
                    count = 0;
                    if (BuildConfig.DEBUG){
                        Log.d(TAG,"Failed to get users count");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                count = 0;
                if (BuildConfig.DEBUG){
                    Log.d(TAG,"Failed to get users count");
                }
            }
        });

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    String userId = snapshot.getKey();

                    if (!userRefs.containsKey(userId)){
                        observeUser(userId);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userId = snapshot.getKey();
                    stopObserve(userId);
                    if (BuildConfig.DEBUG){
                        Log.d(TAG,userId +  "removed observer");
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (BuildConfig.DEBUG){
                    Log.d(TAG,"Failed to observe user: " + error.toString());
                }
            }
        };
        ref.addChildEventListener(listener);

        chatRefAndListener = new Pair<>(ref,listener);
    }

    public void stopListening(){
        if (chatRefAndListener == null || chatId == null){
            return;
        }
        chatRefAndListener.first.removeEventListener(chatRefAndListener.second);
        chatRefAndListener = null;

        for (String userId: userRefs.keySet()){
            stopObserve(userId);
        }

        observableUsers.clear();
        notifyDataChanged(observableUsers.values());
    }

    private void observeUser(String userId){
        DatabaseReference ref = UserListRepository.getUser(userId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    observableUsers.put(userId,user);
                    notifyDataChanged(observableUsers.values());
                    if (BuildConfig.DEBUG){
                        Log.d(TAG,userId +  "added to observer");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (BuildConfig.DEBUG){
                    Log.e(TAG,"Failed to load user: " + error.toString());
                }
            }
        };

        ref.addValueEventListener(listener);
        userRefs.put(userId,ref);
    }

    private void stopObserve(String userId){
        DatabaseReference ref = userRefs.remove(userId);
        ValueEventListener listener = userListeners.remove(userId);
        if (ref != null && listener != null){
            ref.removeEventListener(listener);
        }
    }

    private void notifyDataChanged(Collection<? extends  User> data){
        if (data !=null) {
            List<User> userlist = new ArrayList<>(data);
            Collections.sort(userlist);
            users.postValue(userlist);
            if (data.size() == count){
                loadingEnded.postValue(null);
            }
        }
    }

}
